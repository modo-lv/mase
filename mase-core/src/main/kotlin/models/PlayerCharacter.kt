package models

import content.Gender
import content.Profession
import content.Race
import utils.*

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

    val name: String = bytes.readString(Addresses.NAME, limit = 12)
    val race: Race = bytes.readLeIntEnum<Race>(Race.ADDRESS)
    val profession: Profession = bytes.readLeIntEnum<Profession>(Profession.ADDRESS)
    val gender: Gender = bytes.readLeIntEnum<Gender>(Gender.ADDRESS)

    val signature =
        "$name, a ${gender.toSentenceString()} ${race.toSentenceString()} ${profession.toSentenceString()}"

    object Addresses {
        const val NAME = 0x10
        const val XP = 0x3EE222
    }
}