import Checksum.Companion.CHECKSUM_SIZE
import Player.Companion.readPlayer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian
import java.io.File

typealias SectionMap = MutableMap<Section, MutableList<Int>>

private val logger = KotlinLogging.logger { }

class Save(
    val file: File,
) {
    val sections: SectionMap = mutableMapOf()
    val checksums = mutableListOf<Checksum>()
    val bytes = file.readBytes()

    fun read() {
        readSections()
        readChecksums()
        computeChecksums()
    }

    fun readChecksums() {
        logger.info { "Reading checksums..." }
        checksums.addAll(
            Checksum(
                0 ..< sections[Section.GIVD]!!.single() - CHECKSUM_SIZE,
                xorValue = 0xAEF0FFA0u
            ),
            Checksum(
                sections[Section.GIVD]!!.single() ..< sections[Section.GMTP]!!.single() - CHECKSUM_SIZE,
                xorValue = 0x12345678u
            )
        )

        readLevelChecksums()

        checksums.addAll(
            Checksum(
                bytes.size - CHECKSUM_SIZE * 2,
                xorValue = 0x05afc241u
            ),
            Checksum(
                bytes.size - CHECKSUM_SIZE,
                xorValue = 0u
            )
        )
    }

    fun readLevelChecksums() {
        val levelInfoAddress = sections[Section.LVNF]!!
        val levelMapAddresses = sections[Section.LVMP]!!
        var levelChecksumStart = sections[Section.GMTP]!!.single()
        val player = bytes.readPlayer()
        var levelMapIndex = 0
        var firstLevelSkipped = false
        var xorValue = 0u
        for (location in 1 ..< 51) { // Last checksum is added manually
            for (level in 0 ..< 100) {
                val visited =
                    bytes.leInt(Addresses.LOCATION_VISITED_FLAGS_BASE + (400 * location) + (level * 4)) != 0
                val type = bytes.leInt(levelInfoAddress[location * 100 + level] + Section.HEADER_SIZE)
                //println("Level type for [${loc}:${level}] = 0x${levelType.toHexString(HexFormat.UpperCase)}")
                if (visited || (
                            type in arrayOf(0x41, 0x42, 0x80)
                                    && location.toShort() == player.locationId
                                    && level.toShort() == player.locationLevel)
                ) {
                    val address = levelMapAddresses[levelMapIndex]
                    levelMapIndex++
                    if (firstLevelSkipped) {
                        checksums.addLast(
                            Checksum(
                                data = levelChecksumStart ..< address - CHECKSUM_SIZE,
                                xorValue = xorValue
                            )
                        )
                        levelChecksumStart = address
                    } else {
                        firstLevelSkipped = true
                    }
                    xorValue = (type * location * level).toUInt()
                }
            }
        }
        checksums.addLast(
            Checksum(
                data = levelChecksumStart ..< bytes.size - (CHECKSUM_SIZE * 3),
                xorValue = xorValue,
            )
        )

        if (levelMapIndex != levelMapAddresses.size) {
            throw Exception("There are ${levelMapAddresses.size} level checksums but $levelMapIndex were found.")
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun computeChecksums() {
        var hash = 0xFFFFFFFFu
        checksums.forEach { checksum ->
            for (address in checksum.data) {
                val crcIndex = bytes[address].toUByte().toUInt().xor(hash).toUByte().toInt()
                hash = hash.shr(8).xor(Checksum.CRC32_TABLE[crcIndex])
            }
            hash = hash.xor(checksum.xorValue)

            // Last checksum needs to be flipped
            if (checksum == checksums.last()) {
                hash = hash.inv()
            }

            val fileHash = bytes.leInt((checksum.data.last + 1) + 16)
            logger.debug { "File hash for ${checksum.data}: 0x${fileHash.toHexString()}, our hash: 0x${hash.toHexString()}" }

            hash = hash.xor(checksum.xorValue)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun readSections() {
        logger.info { "Reading sections..." }
        var readPosition = 0
        for (section in Section.entries) {
            logger.debug { "Reading ${section} data..." }
            val needle = section.name.toByteArray(Charsets.US_ASCII) + section.version.toLittleEndian().toByteArray()
            for (count in 1 .. (section.count ?: Int.MAX_VALUE)) {
                if (readPosition >= bytes.size)
                    break
                for (address in readPosition ..< bytes.size) {
                    if (address == bytes.size - 1) {
                        readPosition = bytes.size
                        break
                    }

                    if (bytes[address] == needle[0]
                        && address + Section.HEADER_SIZE - 1 < bytes.size
                        && bytes[address + 1] == needle[1]
                        && bytes[address + 2] == needle[2]
                        && bytes[address + 3] == needle[3]
                        && bytes[address + 4] == needle[4]
                        && bytes[address + 5] == needle[5]
                        && bytes[address + 6] == needle[6]
                        && bytes[address + 7] == needle[7]
                    ) {
                        logger.debug { "Reading ${section}:${count}..." }
                        sections.getOrPut(section) { mutableListOf() }.addLast(address)
                        readPosition = address + (section.size ?: Section.HEADER_SIZE) - 1
                        break
                    }
                }
            }
        }
        Section.entries.find { !sections.containsKey(it) }?.also {
            throw Exception("Failed to find section [${it}], version [${it.version}].")
        }
        Section.entries.find { sections[it]!!.count() != (it.count ?: sections[it]!!.count()) }?.also {
            throw Exception(
                "Section [${it}] should repeat ${it.count} times, but only ${sections[it]!!.count()} were found."
            )
        }

        logger.debug { "Found ${sections.mapValues { it.value.map { "0x${it.toHexString(HexFormat.UpperCase)}" } }}" }
    }
}
