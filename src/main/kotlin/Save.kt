import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian
import java.io.File
import java.lang.Exception

typealias SectionMap = MutableMap<Section, MutableList<Int>>

class Save(
    val file: File,
) {
    val sections: SectionMap = mutableMapOf()

    fun read() {
        readSections()
    }

    fun readSections() {
        println("Reading sections...")
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

        //println("Found ${sections.mapValues { it.value.map { "0x${it.toHexString(HexFormat.UpperCase)}" } }}")
    }
}
