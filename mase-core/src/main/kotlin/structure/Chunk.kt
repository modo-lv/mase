package structure

/** Chunks and their starting addresses in the save data. **/
typealias Chunks = MutableMap<Chunk, MutableList<Int>>

/**
 * Represents an ADOM save data "chunk" specification.
 *
 * An ADOM save file is separated into chunks, each starting with its identifying prefix and version number.
 *
 * Sections are also used to determine data segments for checksum calculations.
 *
 * @param version Version number of the section.
 * @param count How many times in a row does the section repeat.
 *              `null` for sections with a variable repeat count (`LVMP`).
 * @param size If known and same for all, length of the chunk contents (excluding the header).
 */
enum class Chunk(
    val version: Int = 0x01,
    val count: Int? = 1,
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
    PCSK(count = 41, size = 19),
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

    /** Maps of visited levels **/
    LVMP(version = 2, count = null),

    ;

    companion object {
        /** Length of a section header -- the name and the version number -- in bytes */
        const val HEADER_SIZE = 8
    }
}