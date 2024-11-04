import io.github.oshai.kotlinlogging.KotlinLogging
import models.Player
import structure.*
import utils.leWrite

/**
 * Represents a saved game and contains most of the methods for interacting with it.
 * @param bytes The raw contents of the save file.
 */
open class SaveData<T : SaveData<T>>(val bytes: ByteArray) {
    @Suppress("UNCHECKED_CAST")
    val self = this as T
    private val logger = KotlinLogging.logger { }


    /**
     * Save game section data, automatically loaded on first access.
     *
     * Since sections don't change outside of gameplay, this only needs to be done once per save file read.
     */
    val sections: Sections by lazy { bytes.findSections() }

    /**
     * Save game checksum segment data, automatically loaded on first access.
     *
     * Since checksum segments don't change outside of gameplay,
     * this only needs to be done once per save file read.
     */
    val checksumSegments: ChecksumSegments by lazy { bytes.findChecksumSegments(sections) }


    /**
     * Access to player properties in the current data.
     */
    val player: Player = Player(bytes)


    /**
     * (Re-)calculates checksums for each checksum segment, form the data currently in [bytes].
     */
    open fun computeChecksums(): T {
        if (checksumSegments.isEmpty()) {
            logger.warn { "Checksum segment list is empty, nothing to calculate. " }
            return self
        }
        var hash = 0xFFFFFFFFu
        checksumSegments.forEach { segment ->
            hash = segment.compute(hash, bytes, segment == checksumSegments.last()).xor(segment.xorValue)
        }
        return self
    }

    /**
     * Updates stored checksums with the currently calculated values.
     */
    fun fixChecksums(): T {
        computeChecksums()
        checksumSegments.forEach { segment ->
            if (segment.isMismatched()) {
                logger.debug { "Checksum [${segment.range}] is mismatched, updating..." }
                val header = mutableListOf<UInt>()
                header.add(0, segment.computedChecksum / 50u)
                header.add(1, header[0] - segment.computedChecksum)
                header.add(2, header[0] * header[1])
                header.add(3, header[2] xor header[1])
                header.add(4, segment.computedChecksum)
                bytes.leWrite(segment.range.last + 1, header)
                segment.readStored(bytes)
            }
        }
        return self
    }


    companion object {
        /**
         * Shorthand for getting the address of a section.
         *
         * @param section Section to get the starting address of.
         * @param addressIndex Which address to get.
         */
        fun Sections.address(section: Section, addressIndex: Int = 0): Int {
            return this[section]!![addressIndex]
        }
    }
}