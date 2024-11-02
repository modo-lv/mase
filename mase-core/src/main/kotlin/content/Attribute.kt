package content

import content.Attribute.Addresses.BASE
import content.Attribute.Addresses.HISTORY
import content.Attribute.Addresses.MAX

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

    val baseAddress = BASE + (offset * 4)
    val maxAddress = MAX + (offset * 4)
    val historyAddress = HISTORY + (offset * 4)

    object Addresses {
        /** Starting address for base stat values */
        const val BASE = 0x3EE266

        /** Starting address for maximum stat values */
        const val MAX = 0x3EE28A

        /** Starting address for stat change history */
        const val HISTORY = 0x3EE2AE
    }
}