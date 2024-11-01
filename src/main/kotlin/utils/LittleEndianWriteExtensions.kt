package utils

import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian

/**
 * Write a [UInt] value, using little-endian ordering, into 4 bytes starting at [index].
 */
fun ByteArray.writeLeUInt(value: UInt, index: Int) {
    value.toInt().toLittleEndian().copyInto(this, index)
}

/**
 * Write a [ULong] value, using little-endian ordering, into 8 bytes starting at [index].
 */
fun ByteArray.writeLeULong(value: ULong, index: Int) {
    value.toLong().toLittleEndian().copyInto(this, index)
}