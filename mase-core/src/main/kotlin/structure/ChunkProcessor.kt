package structure

import io.github.oshai.kotlinlogging.KotlinLogging
import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian
import utils.toHex

private val logger = KotlinLogging.logger { }

/**
 * Reads section data from a saved game (byte array).
 */
fun ByteArray.findSections(): Chunks {
    logger.info { "Reading chunks..." }
    var readPosition = 0
    val result: Chunks = mutableMapOf()

    for (section in Chunk.entries) {
        logger.debug { "Looking for ${section.count ?: "all of"} [$section] chunk(s)..." }
        val needle =
            section.name.toByteArray(Charsets.US_ASCII) + section.version.toLittleEndian().toByteArray()
        for (count in 1 .. (section.count ?: Int.MAX_VALUE)) {
            if (readPosition >= this.size)
                break
            for (address in readPosition ..< this.size) {
                if (address == this.size - 1) {
                    readPosition = this.size
                    break
                }

                if (this[address] == needle[0]
                    && address + Chunk.HEADER_SIZE - 1 < this.size
                    && this[address + 1] == needle[1]
                    && this[address + 2] == needle[2]
                    && this[address + 3] == needle[3]
                    && this[address + 4] == needle[4]
                    && this[address + 5] == needle[5]
                    && this[address + 6] == needle[6]
                    && this[address + 7] == needle[7]
                ) {
                    logger.trace { "Found [${section}]:${count} at ${address.toHex()}" }
                    result.getOrPut(section) { mutableListOf() }.addLast(address)
                    readPosition = address + (section.size ?: Chunk.HEADER_SIZE) - 1
                    break
                }
            }
        }
    }
    Chunk.entries.find { !result.containsKey(it) }?.also {
        throw Exception("Failed to find section [${it}], version [${it.version}].")
    }
    Chunk.entries.find { result[it]!!.count() != (it.count ?: result[it]!!.count()) }?.also {
        throw Exception(
            "Section [${it}] should repeat ${it.count} times, but ${result[it]!!.count()} were found."
        )
    }

    if (logger.isDebugEnabled()) {
        val output = result.mapValues { entry ->
            StringBuilder().also { sb ->
                if (entry.value.size == 1 || logger.isTraceEnabled())
                    sb.append(entry.value.map { it.toHex() })
                else
                    sb.append("[${entry.value.first().toHex()}, ..](${entry.value.size})")
            }
        }
        logger.debug { "Found all ${Chunk.entries.size} sections: " }
        if (logger.isTraceEnabled())
            logger.trace { output }
        else
            logger.debug { output }
    } else {
        logger.info {
            "Found ${result.values.sumOf { it.size }} sections (including repeated) " +
                    "of all ${Chunk.entries.size} types."
        }
    }
    return result
}