package structure

/**
 * @param count How many times in a row does the section repeat.
 * @param size If known & static
 */
enum class Section(
    val version: Int = 0x01,
    val count: Int? = 1, // null means unknown count, try to read all there are
    val size: Int? = null,
) {
    ADOM(version = 2),
    PLYR(version = 0x1E),
    LVNF(count = 5100, size = 125),
    GMCP(version = 2),
    GDSD,
    GEVT,
    GIVD,
    GSPD,
    GITD,
    GSKD,
    GMTP,
    GMST,
    GBSS,
    GMAS,
    AMMY,
    GCHD,
    GRLG,
    GWPS,

    /** Weapon skills **/
    WPNS(count = 19), // 10 melee + 8 ranged + 1 shield
    GHNT,
    ALVL,
    LVMP(version = 2, count = null),

    ;

    companion object {
        /** Size of a section header -- the name and the version number */
        const val HEADER_SIZE = 8
    }
}