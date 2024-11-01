/**
 * Known addresses of values in the save file.
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
    const val LOCATION = 41

    /**
     * How deep the player is in the current location/dungeon.
     *
     * Caverns of Chaos is a special case, see [LOCATION] for details.
     */
    const val LOCATION_LEVEL = 43
}