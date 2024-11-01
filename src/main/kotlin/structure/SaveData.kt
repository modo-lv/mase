package structure

import gui.tabs.Model
import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian
import player.Location
import player.PlayerLocation.Companion.readPlayer
import utils.leInt
import utils.leULong
import utils.writeLeUInt
import utils.writeLeULong

typealias SectionMap = MutableMap<Section, MutableList<Int>>

open class SaveData(val data: ByteArray) {
    private val logger = KotlinLogging.logger { }

    val sections: SectionMap = mutableMapOf()
    val checksums: ObservableList<Checksum> = FXCollections.observableArrayList()

    fun read() {
        readSections()
        readChecksums()
        computeChecksums()
        readPlayer()
    }

    fun readPlayer() {
        val xpAddr = 0x003EE222
        Model.xpProperty.value = data.leULong(xpAddr).toDouble()
    }

    fun updateXp(newXp: ULong) {
        logger.trace { "Updating XP to ${newXp}..." }
        val xpAddr = 0x003EE222
        data.writeLeULong(newXp, xpAddr)
    }

    fun fixChecksums() {
        this.checksums.forEach {
            if (it.isMismatched()) {
                logger.info { "Checksum [${it.segment}] is mismatched, updating..." }
                val pos = it.segment.last + 1
                val mangle1 = it.computedHash / 50u
                val mangle2 = mangle1 - it.computedHash
                val mangle3 = mangle1 * mangle2
                val mangle4 = mangle3 xor mangle2

                data.writeLeUInt(mangle1, pos)
                data.writeLeUInt(mangle2, pos + 4)
                data.writeLeUInt(mangle3, pos + 8)
                data.writeLeUInt(mangle4, pos + 12)
                data.writeLeUInt(it.computedHash, pos + 16)
            }
        }
        computeChecksums()
    }

    fun readChecksums() {
        if (this.checksums.isEmpty()) {
            logger.warn { "structure.Section data missing, reading that first." }
            readSections()
        }
        logger.info { "Reading checksums..." }
        checksums.clear()
        checksums.addAll(
            Checksum(
                0 ..< sections[Section.GIVD]!!.single() - Checksum.CHECKSUM_SIZE,
                xorValue = 0xAEF0FFA0u
            ),
            Checksum(
                sections[Section.GIVD]!!.single() ..< sections[Section.GMTP]!!.single() - Checksum.CHECKSUM_SIZE,
                xorValue = 0x12345678u
            )
        )

        readLevelChecksums()

        checksums.addAll(
            Checksum(
                data.size - Checksum.CHECKSUM_SIZE * 2,
                xorValue = 0x05afc241u
            ),
            Checksum(
                data.size - Checksum.CHECKSUM_SIZE,
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
                    data.leInt(Location.Addresses.LOCATION_VISITED_FLAGS_BASE + (400 * location) + (level * 4)) != 0
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
                                segment = levelChecksumStart ..< address - Checksum.CHECKSUM_SIZE,
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
                segment = levelChecksumStart ..< data.size - (Checksum.CHECKSUM_SIZE * 3),
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
            logger.debug { "Reading $section data..." }
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
                "structure.Section [${it}] should repeat ${it.count} times, but ${sections[it]!!.count()} were found."
            )
        }

        logger.debug { "Found ${sections.mapValues { it.value.map { "0x${it.toHexString(HexFormat.UpperCase)}" } }}" }
    }
}