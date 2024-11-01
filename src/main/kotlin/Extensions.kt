import org.kotlincrypto.endians.LittleEndian

/**
 * Read 4 bytes from a byte array in little-endian order and convert to an [Int].
 */
fun ByteArray.leInt(index: Int): Int =
    LittleEndian.bytesToInt(this[index], this[index + 1], this[index + 2], this[index + 3])

/**
 * Read 2 bytes from a byte array in little-endian order and convert to a [Short].
 */
fun ByteArray.leShort(index: Int): Short =
    LittleEndian.bytesToShort(this[index], this[index + 1])


fun <T> MutableList<T>.addAll(vararg elements: T) {
    this.addAll(elements)
}