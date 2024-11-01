@file:OptIn(ExperimentalStdlibApi::class)

package utils

import org.kotlincrypto.endians.LittleEndian

fun Int.toHex() = "0x${this.toHexString(HexFormat.UpperCase)}"
fun UInt.toHex() = "0x${this.toHexString(HexFormat.UpperCase)}"


/**
 * Read 4 bytes from a byte array in little-endian order and convert to an [Int].
 */
fun ByteArray.leInt(index: Int): Int =
    LittleEndian.bytesToInt(this[index], this[index + 1], this[index + 2], this[index + 3])

/**
 * Read 4 bytes from a byte array in little-endian order and convert to an [UInt].
 */
fun ByteArray.leUInt(index: Int): UInt =
    LittleEndian.bytesToInt(this[index], this[index + 1], this[index + 2], this[index + 3]).toUInt()

/**
 * Read 2 bytes from a byte array in little-endian order and convert to a [Short].
 */
fun ByteArray.leShort(index: Int): Short =
    LittleEndian.bytesToShort(this[index], this[index + 1])