package content

import content.Attribute.Addresses.BASE
import content.Attribute.Addresses.HISTORY
import content.Attribute.Addresses.MAX

/**
 * @param offset How many attributes come before this one in save data.
 */
sealed class Attribute(val offset: Int) {
    class Strength : Attribute(0)
    class Learning : Attribute(1)
    class Willpower : Attribute(2)
    class Dexterity : Attribute(3)
    class Toughness : Attribute(4)
    class Charisma : Attribute(5)
    class Appearance : Attribute(6)
    class Mana : Attribute(7)
    class Perception : Attribute(8)

    val baseValueAddress = BASE + (offset * 4)
    val maxValueAddress = MAX + (offset * 4)

    /**
     * Address where the "was changed by +/-XX" value is stored for this attribute.
     */
    val historyValueAddress = HISTORY + (offset * 4)

    object Addresses {
        /** Starting address for base stat values */
        const val BASE = 0x3EE266

        /** Starting address for maximum stat values */
        const val MAX = 0x3EE28A

        /** Starting address for stat change history */
        const val HISTORY = 0x3EE2AE
    }
}