class Player(
    val locationId: Short,
    val locationLevel: Short,
) {
    val location = Location.entries.find { it.value == locationId }

    companion object {
        fun ByteArray.readPlayer(): Player {
            val loc = this.leShort(Addresses.LOCATION)
            val level = this.leShort(Addresses.LOCATION_LEVEL)
            return if (level == 0.toShort()) { // CoC
                Player(
                    locationId = 1,
                    locationLevel = loc
                )
            } else {
                Player(
                    locationId = loc,
                    locationLevel = level
                )
            }
        }
    }
}