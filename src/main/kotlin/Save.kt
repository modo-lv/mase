import io.github.oshai.kotlinlogging.KotlinLogging
import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian
import java.io.File

typealias SectionMap = MutableMap<Section, MutableList<Int>>

private val logger = KotlinLogging.logger { }

class Save(
    val file: File,
) {
    val sections: SectionMap = mutableMapOf()
    val checksums = mutableListOf<Checksum>()

    fun read() {
        readSections()
        readChecksums()
        computeChecksums()
    }

    fun readChecksums() {
        logger.info { "Reading checksums..." }
        val bytes = file.readBytes()
        var readPosition = 0
        checksums.addLast(
            Checksum(
                0..<sections[Section.GIVD]!!.first() - Checksum.CHECKSUM_SIZE,
                xorValue = 0xAEF0FFA0u
            )
        )
        checksums.addLast(
            Checksum(
                sections[Section.GIVD]!!.first()..<sections[Section.GMTP]!!.first() - Checksum.CHECKSUM_SIZE,
                xorValue = 0x12345678u
            )
        )

    }

    @OptIn(ExperimentalStdlibApi::class)
    fun computeChecksums() {
        val bytes = file.readBytes()
        var hash = 0xFFFFFFFFu
        checksums.forEach { checksum ->
            for (address in checksum.data) {
                val crcIndex = bytes[address].toUByte().toUInt().xor(hash).toUByte().toInt()
                hash = hash.shr(8).xor(Checksum.CRC32_TABLE[crcIndex])
            }
            hash = hash.xor(checksum.xorValue)

            // Last checksum needs to be flipped
            /*if (checksum == checksums.last()) {
                hash = hash.inv()
            }*/

            val fileHash = bytes.leInt((checksum.data.last + 1) + 16)
            logger.debug { "File hash: 0x${fileHash.toHexString()}, our hash: 0x${hash.toHexString()}" }

            hash = hash.xor(checksum.xorValue)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun readSections() {
        logger.info { "Reading sections..." }
        val bytes = file.readBytes()
        var readPosition = 0
        for (section in Section.entries) {
            val needle = section.name.toByteArray(Charsets.US_ASCII) + section.version.toLittleEndian().toByteArray()
            for (count in 1..section.count) {
                for (address in readPosition..<bytes.size) {
                    if (bytes[address] == needle[0]
                        && address + Section.HEADER_SIZE - 1 < bytes.size
                        && bytes[address + 1] == needle[1]
                        && bytes[address + 2] == needle[2]
                        && bytes[address + 3] == needle[3]
                        && bytes[address + 4] == needle[4]
                        && bytes[address + 5] == needle[5]
                        && bytes[address + 6] == needle[6]
                        && bytes[address + 7] == needle[7]
                    ) {
                        sections.getOrPut(section) { mutableListOf() }.addLast(address)
                        readPosition = address + (section.size ?: Section.HEADER_SIZE) - 1
                        break
                    }
                }
            }
        }
        Section.entries.find { !sections.containsKey(it) }?.also {
            throw Exception("Failed to find section [${it}], version [${it.version}].")
        }
        Section.entries.find { sections[it]!!.count() != it.count }?.also {
            throw Exception(
                "Section [${it}] should repeat ${it.count} times, but only ${sections[it]!!.count()} were found."
            )
        }

        logger.debug { "Found ${sections.mapValues { it.value.map { "0x${it.toHexString(HexFormat.UpperCase)}" } }}" }
    }
}
