package content

/**
 * Skills in the game.
 */
enum class Skill(override val id: Int) : WithIntId {
    Alertness(0x01),
    Appraising(0x02),
    Archery(0x03),
    Athletics(0x04),
    Backstabbing(0x05),
    BridgeBuilding(0x06),
    Climbing(0x07),
    Concentration(0x08),
    Cooking(0x09),
    Courage(0x0A),
    DetectItemStatus(0x0B),
    DetectTraps(0x0C),
    DisarmTraps(0x0D),
    Dodge(0x0E),
    FindWeakness(0x0F),
    FirstAid(0x10),
    Fletchery(0x11),
    FoodPreservation(0x12),
    Gardening(0x13),
    Gemology(0x14),
    Haggling(0x15),
    Healing(0x16),
    Herbalism(0x17),
    Law(0x18),
    Listening(0x19),
    Literacy(0x1A),
    Metallurgy(0x1B),
    Mining(0x1C),
    Music(0x1D),
    Necromancy(0x1E),
    PickLocks(0x1F),
    PickPockets(0x20),
    Smithing(0x21),
    Stealth(0x22),
    Survival(0x23),
    Swimming(0x24),
    Tactics(0x25),
    TwoWeaponCombat(0x26),
    Ventriloquism(0x27),
    Woodcraft(0x28),

    Nothing(0x2A);
}