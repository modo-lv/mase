package models

import content.Gender
import content.Profession
import content.Race
import utils.*

/**
 * Miscellaneous player character properties -- attributes, level, xp, etc.
 */
class PlayerCharacter(val bytes: ByteArray) {
    var level: Short
        get() = bytes.leNum<Short>(Addresses.LEVEL)
        set(value) = bytes.leWrite(value, Addresses.LEVEL)

    var xp: Long
        get() = bytes.leNum<Long>(Addresses.XP)
        set(value) = bytes.leWrite(value, Addresses.XP)

    var alignment: Int
        get() = bytes.leNum<Int>(Addresses.ALIGNMENT)
        set(value) = bytes.leWrite(value, Addresses.ALIGNMENT)

    val name: String = bytes.readString(Addresses.NAME, limit = 12)
    val race: Race = bytes.leEnum<Race>(Race.ADDRESS)
    val profession: Profession = bytes.leEnum<Profession>(Profession.ADDRESS)
    val gender: Gender = bytes.leEnum<Gender>(Gender.ADDRESS)

    val signature =
        "$name, a ${gender.toSentenceString()} ${race.toSentenceString()} ${profession.toSentenceString()}"

    object Addresses {
        const val NAME = 0x10
        const val LEVEL = 0x3EE1FC
        const val XP = 0x3EE222
        const val ALIGNMENT = 0x489FAA
    }
}