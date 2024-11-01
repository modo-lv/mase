/**
 * Locations that the player can be in.
 * There are 51 potential values, 1-51.
 */
enum class Location(val value: Short) {
    CavernsOfChaos(1), // Special case, see address constants for explanation
    Wilderness(4),
    Terinyo(5),
    Lawenilothehl(22),
    PuppyCave(23),
    UnremarkableCave(26),
    SmallCave(28),
    BarbariansClearing(29),
    GoblinCamp(34),
    InfiniteDungeon(44),
    WildernessEntered(45),
    MoldyDungeon(45),
    ThievesGuild(48),
    DustyDungeon(49),
}