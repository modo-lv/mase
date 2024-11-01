import Checksum.Companion.CHECKSUM_SIZE
import Player.Companion.readPlayer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian

typealias SectionMap = MutableMap<Section, MutableList<Int>>

private val logger = KotlinLogging.logger { }

open class SaveData(val data: ByteArray) {
    val sections: SectionMap = mutableMapOf()
    val checksums = mutableListOf<Checksum>()

    fun read() {
        readSections()
        readChecksums()
        computeChecksums()
    }

    fun readChecksums() {
        if (this.checksums.isEmpty()) {
            logger.warn { "Section data missing, reading that first." }
            readSections()
        }
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
                data.size - CHECKSUM_SIZE * 2,
                xorValue = 0x05afc241u
            ),
            Checksum(
                data.size - CHECKSUM_SIZE,
                xorValue = 0u
            )
        )
    }

    fun readLevelChecksums() {
        val levelInfoAddress = sections[Section.LVNF]!!
        val levelMapAddresses = sections[Section.LVMP]!!
        var levelChecksumStart = sections[Section.GMTP]!!.single()
        val player = data.readPlayer()
        var levelMapIndex = 0
        var firstLevelSkipped = false
        var xorValue = 0u
        for (location in 1 ..< 51) { // Last checksum is added manually
            for (level in 0 ..< 100) {
                val visited =
                    data.leInt(Addresses.LOCATION_VISITED_FLAGS_BASE + (400 * location) + (level * 4)) != 0
                val type = data.leInt(levelInfoAddress[location * 100 + level] + Section.HEADER_SIZE)
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
                                segment = levelChecksumStart ..< address - CHECKSUM_SIZE,
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
                segment = levelChecksumStart ..< data.size - (CHECKSUM_SIZE * 3),
                xorValue = xorValue,
            )
        )

        if (levelMapIndex != levelMapAddresses.size) {
            throw Exception("There are ${levelMapAddresses.size} level checksums but $levelMapIndex were found.")
        }
    }

    fun computeChecksums() {
        if (this.checksums.isEmpty()) {
            logger.warn { "Checksum data missing, reading that first." }
            readChecksums()
        }
        var hash = 0xFFFFFFFFu
        checksums.forEach { checksum ->
            hash = checksum.compute(hash, data, checksum == checksums.last())
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun readSections() {
        logger.info { "Reading sections..." }
        var readPosition = 0
        sections.clear()
        for (section in Section.entries) {
            logger.debug { "Reading ${section} data..." }
            val needle = section.name.toByteArray(Charsets.US_ASCII) + section.version.toLittleEndian().toByteArray()
            for (count in 1 .. (section.count ?: Int.MAX_VALUE)) {
                if (readPosition >= data.size)
                    break
                for (address in readPosition ..< data.size) {
                    if (address == data.size - 1) {
                        readPosition = data.size
                        break
                    }

                    if (data[address] == needle[0]
                        && address + Section.HEADER_SIZE - 1 < data.size
                        && data[address + 1] == needle[1]
                        && data[address + 2] == needle[2]
                        && data[address + 3] == needle[3]
                        && data[address + 4] == needle[4]
                        && data[address + 5] == needle[5]
                        && data[address + 6] == needle[6]
                        && data[address + 7] == needle[7]
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
                "Section [${it}] should repeat ${it.count} times, but ${sections[it]!!.count()} were found."
            )
        }

        logger.debug { "Found ${sections.mapValues { it.value.map { "0x${it.toHexString(HexFormat.UpperCase)}" } }}" }
    }
}
