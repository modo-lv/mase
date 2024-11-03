package content

enum class Profession(id: Int) {
    Fighter(0x00),
    Paladin(0x01),
    Ranger(0x02),
    Thief(0x03),
    Assassin(0x04),
    Wizard(0x05),
    Priest(0x06),
    Bard(0x07),
    Monk(0x08),
    Healer(0x09),
    Weaponsmith(0x0A),
    Archer(0x0B),
    Merchant(0x0C),
    Farmer(0x0D),
    Mindcrafter(0x0E),
    Barbarian(0x0F),
    Druid(0x10),
    Necromancer(0x11),
    Elementalist(0x12),
    Beastfighter(0x13),
    ChaosKnight(0x14),
    Duelist(0x15),

    ;

    companion object {
        const val ADDRESS = 0x21
    }
}