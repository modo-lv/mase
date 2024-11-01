package player

import player.Stat.Addresses.BASE
import player.Stat.Addresses.HISTORY
import player.Stat.Addresses.MAX

sealed class Stat(val offset: Int) {
    class Strength: Stat(0)
    class Learning: Stat(1)
    class Willpower: Stat(2)
    class Dexterity: Stat(3)
    class Toughness: Stat(4)
    class Charisma: Stat(5)
    class Appearance: Stat(6)
    class Mana: Stat(7)
    class Perception: Stat(8)

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