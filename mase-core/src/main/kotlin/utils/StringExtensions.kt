package utils

/**
 * Read a 0-terminated ASCII string from a byte array.
 * @limit Maximum length of the string to read.
 */
fun ByteArray.readString(index: Int, limit: Int = 256): String = this
    .slice(index .. index + limit)
    .takeWhile { it != 0.toByte() }
    .toByteArray()
    .toString(Charsets.US_ASCII)


fun Enum<*>.toSignatureString() = this
    .toSentenceString()
    .lowercase()

fun Enum<*>.toSentenceString() = this
    .name
    .replace("(.)([A-Z])".toRegex(), "$1 $2")


fun IntRange.toDisplayString() =
    if (this.first == this.last)
        "${this.last}"
    else
        "${this}"