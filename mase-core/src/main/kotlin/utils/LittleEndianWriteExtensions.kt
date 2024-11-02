package utils

import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian

/**
 * Write a [UInt] value, using little-endian ordering, into 4 bytes starting at [index].
 */
fun ByteArray.writeLeUInt(value: UInt, index: Int) {
    value.toInt().toLittleEndian().copyInto(this, index)
}

fun ByteArray.writeLeUInts(startAt: Int, vararg values: UInt) {
    values.forEachIndexed { i, value ->
        value.toInt().toLittleEndian().copyInto(this, startAt + (i * 4))
    }
}

/**
 * Write a [ULong] value, using little-endian ordering, into 8 bytes starting at [index].
 */
fun ByteArray.writeLeULong(value: ULong, index: Int) {
    value.toLong().toLittleEndian().copyInto(this, index)
}