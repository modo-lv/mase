package content

import content.PrimaryAttribute.Addresses.BASE
import content.PrimaryAttribute.Addresses.HISTORY
import content.PrimaryAttribute.Addresses.MAX

/**
 * @param offset How many attributes come before this one in save data.
 */
sealed class PrimaryAttribute(val offset: Int) {
    class Strength : PrimaryAttribute(0)
    class Learning : PrimaryAttribute(1)
    class Willpower : PrimaryAttribute(2)
    class Dexterity : PrimaryAttribute(3)
    class Toughness : PrimaryAttribute(4)
    class Charisma : PrimaryAttribute(5)
    class Appearance : PrimaryAttribute(6)
    class Mana : PrimaryAttribute(7)
    class Perception : PrimaryAttribute(8)

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