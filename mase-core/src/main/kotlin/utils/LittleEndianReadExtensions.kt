package utils

import content.WithIntId
import org.kotlincrypto.endians.LittleEndian

@ExperimentalStdlibApi fun Int.toHex() = "0x${this.toHexString(HexFormat.UpperCase)}"
@ExperimentalStdlibApi fun UInt.toHex() = "0x${this.toHexString(HexFormat.UpperCase)}"
@ExperimentalStdlibApi fun IntRange.toHex() = "${this.first.toHex()}..${this.last.toHex()}"

/**
 * Read 4 bytes from a byte array in little-endian order and convert to an [Int].
 */
fun ByteArray.readLeInt(index: Int): Int =
    LittleEndian.bytesToInt(this[index], this[index + 1], this[index + 2], this[index + 3])

/**
 * Read 4 bytes from a byte array in little-endian order and convert to an [UInt].
 */
fun ByteArray.readLeUInt(index: Int): UInt =
    LittleEndian.bytesToInt(this[index], this[index + 1], this[index + 2], this[index + 3]).toUInt()

/**
 * Read 8 bytes from a byte array in little-endian order and convert to an [ULong].
 */
fun ByteArray.readLeULong(index: Int): ULong =
    LittleEndian.bytesToLong(
        this[index + 0], this[index + 1], this[index + 2], this[index + 3],
        this[index + 4], this[index + 5], this[index + 6], this[index + 7],
    ).toULong()

/**
 * Read 2 bytes from a byte array in little-endian order and convert to a [Short].
 */
fun ByteArray.readLeShort(index: Int): Short =
    LittleEndian.bytesToShort(this[index], this[index + 1])

/**
 * Read
 */
inline fun <reified T> ByteArray.readLeIntEnum(index: Int): T where T: Enum<T>, T: WithIntId =
    enumValues<T>().single { it.id == this.readLeInt(index) }