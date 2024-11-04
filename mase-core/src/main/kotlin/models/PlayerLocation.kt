package models

import player.Location
import utils.leNum

/**
 * Combines location-related data -- dungeon/town, level, coordinates, etc.
 */
class PlayerLocation(
    val locationId: Short,
    val locationLevel: Short,
) {
    val location = Location.entries.find { it.value == locationId }

    companion object {
        fun ByteArray.readPlayerLocation(): PlayerLocation {
            val loc = this.leNum<Short>(Location.Addresses.LOCATION)
            val level = this.leNum<Short>(Location.Addresses.LOCATION_LEVEL)
            return if (level == 0.toShort()) { // CoC
                PlayerLocation(
                    locationId = 1,
                    locationLevel = loc
                )
            } else {
                PlayerLocation(
                    locationId = loc,
                    locationLevel = level
                )
            }
        }
    }
}