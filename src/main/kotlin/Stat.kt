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

    val baseAddress = BASE_ADDRESS + (offset * 4)
    val maxAddress = MAX_ADDRESS + (offset * 4)
    val historyAddress = HISTORY_ADDRESS + (offset * 4)

    companion object {
        /** Starting address for base stat values */
        const val BASE_ADDRESS = 0x3EE266

        /** Starting address for maximum stat values */
        const val MAX_ADDRESS = 0x3EE28A

        /** Starting address for stat change history */
        const val HISTORY_ADDRESS = 0x3EE2AE
    }
}