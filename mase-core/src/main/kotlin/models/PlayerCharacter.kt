package models

import utils.readLeULong
import utils.writeLeULong

/**
 * Miscellaneous player character properties -- attributes, level, xp, etc.
 */
class PlayerCharacter(val bytes: ByteArray) {
    /**
     * Experience points.
     */
    var xp: ULong
        get() = bytes.readLeULong(Addresses.XP)
        set(value) = bytes.writeLeULong(value, Addresses.XP)

    object Addresses {
        const val XP = 0x003EE222
    }
}