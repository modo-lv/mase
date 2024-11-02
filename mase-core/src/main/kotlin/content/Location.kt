package player

/**
 * Locations that the player can be in.
 * There are 51 potential values, 1-51.
 */
enum class Location(val value: Short) {
    CavernsOfChaos(0x01), // Special case, see address constants for explanation
    Wilderness(0x04),
    Terinyo(0x05),
    Lawenilothehl(0x16),
    PuppyCave(0x17),
    UnremarkableCave(0x1A),
    SmallCave(0x1C),
    BarbariansClearing(0x1D),
    GoblinCamp(0x22),
    InfiniteDungeon(0x2C),
    WildernessEntered(0x2D),
    MoldyDungeon(0x2D),
    ThievesGuild(0x30),
    DustyDungeon(0x31),

    ;

    /**
     * Known addresses of location values in the save data.
     */
    object Addresses {
        /**
         * Which location/dungeon the player is currently in.
         *
         * Normally the value at this address matches a location (see [Location]),
         * and [LOCATION_LEVEL] indicates which level of the dungeon/location player is
         *
         * In Caverns of Chaos though, it's reversed - the value at [LOCATION_LEVEL] is always `0`,
         * and instead the value at [LOCATION] indicates which level of CoC the player is on.
         */
        const val LOCATION = 0x29

        /**
         * How deep the player is in the current location/dungeon.
         *
         * Caverns of Chaos is a special case, see [LOCATION] for details.
         */
        const val LOCATION_LEVEL = 0x2B

        /**
         * Starting address for reading location visitation flags.
         *
         * The actual data for each location is 400 bytes of other stuff,
         * followed by 100 integers, one for each possible level.
         */
        const val LOCATION_VISITED_FLAGS_BASE = 0x3E41AD
    }
}