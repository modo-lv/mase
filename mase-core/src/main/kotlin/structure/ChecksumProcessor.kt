package structure

import SaveData.Companion.address
import io.github.oshai.kotlinlogging.KotlinLogging
import player.Location
import models.PlayerLocation.Companion.readPlayerLocation
import utils.addAll
import utils.leNum

private val logger = KotlinLogging.logger { }

/**
 * Finds checksum segments in a saved game (byte array).
 */
fun ByteArray.findChecksumSegments(sections: Sections): ChecksumSegments {
    logger.info { "Finding checksum segments..." }

    val result = mutableListOf<ChecksumSegment>()

    result.addAll(
        ChecksumSegment(
            0 ..< sections[Section.GIVD]!!.single() - ChecksumSegment.CHECKSUM_SIZE,
            xorValue = 0xAEF0FFA0u
        ),
        ChecksumSegment(
            sections[Section.GIVD]!!.single() ..< sections[Section.GMTP]!!.single() - ChecksumSegment.CHECKSUM_SIZE,
            xorValue = 0x12345678u
        )
    )


    val levelInfoAddresses = sections[Section.LVNF]!!
    val levelMapAddresses = sections[Section.LVMP]!!
    var levelChecksumStart = sections.address(Section.GMTP)
    val playerLoc = readPlayerLocation()
    var levelMapIndex = 0
    var firstLevelSkipped = false
    var xorValue = 0u
    for (location in 1 ..< 51) { // Last checksum is added manually
        for (level in 0 ..< 100) {
            val visited = leNum<Int>(
                Location.Addresses.LOCATION_VISITED_FLAGS_BASE + (400 * location) + (level * 4)
            ) != 0
            val type = leNum<Int>(levelInfoAddresses[location * 100 + level] + Section.HEADER_SIZE)
            if (visited || (
                        // Type meaning unknown,
                        // values copied from savadomer (https://gitlab.com/mikesc/savadomer)
                        type in arrayOf(0x41, 0x42, 0x80)
                                && location.toShort() == playerLoc.locationId
                                && level.toShort() == playerLoc.locationLevel)
            ) {
                val address = levelMapAddresses[levelMapIndex]
                levelMapIndex++
                if (firstLevelSkipped) {
                    result.addLast(
                        ChecksumSegment(
                            range = levelChecksumStart ..< address - ChecksumSegment.CHECKSUM_SIZE,
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
    result.addLast(
        ChecksumSegment(
            range = levelChecksumStart ..< this.size - (ChecksumSegment.CHECKSUM_SIZE * 3),
            xorValue = xorValue,
        )
    )
    if (levelMapIndex != levelMapAddresses.size) {
        throw Exception(
            "There are ${levelMapAddresses.size} [LVMP] sections, but only $levelMapIndex checksum segments were found."
        )
    }


    result.addAll(
        ChecksumSegment(
            this.size - ChecksumSegment.CHECKSUM_SIZE * 2,
            xorValue = 0x05afc241u
        ),
        ChecksumSegment(
            this.size - ChecksumSegment.CHECKSUM_SIZE,
            xorValue = 0u
        )
    )

    return result
}