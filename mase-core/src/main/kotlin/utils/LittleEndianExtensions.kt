package utils

import content.WithIntId
import org.kotlincrypto.endians.LittleEndian
import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian

/**
 * Read an enum value from this [ByteArray], 
 * matching by the [WithIntId.id] property (using little-endian ordering).
 */
inline fun <reified T> ByteArray.leEnum(index: Int): T where T : Enum<T>, T : WithIntId =
    enumValues<T>().single { it.id == this.leNum<Int>(index) }


/**
 * Read a number from this [ByteArray], using little-endian ordering.
 */
inline fun <reified T> ByteArray.leNum(index: Int): T {
    return when (T::class) {
        Short::class ->
            LittleEndian.bytesToShort(this[index], this[index + 1])

        Int::class ->
            LittleEndian.bytesToInt(this[index], this[index + 1], this[index + 2], this[index + 3])

        UInt::class ->
            LittleEndian.bytesToInt(this[index], this[index + 1], this[index + 2], this[index + 3]).toUInt()

        Long::class -> LittleEndian.bytesToLong(
            this[index + 0], this[index + 1], this[index + 2], this[index + 3],
            this[index + 4], this[index + 5], this[index + 6], this[index + 7],
        )

        ULong::class -> LittleEndian.bytesToLong(
            this[index + 0], this[index + 1], this[index + 2], this[index + 3],
            this[index + 4], this[index + 5], this[index + 6], this[index + 7],
        ).toULong()

        else -> throw IllegalArgumentException("Unrecognized number type [${T::class.simpleName}]")
    } as T
}

/**
 * Write the bytes of multiple sequential numbers into this [ByteArray], in little-endian order.
 */
fun ByteArray.leWrite(startAt: Int, values: List<Any>) {
    values.forEachIndexed { i, value ->
        val length = when (value) {
            is Byte, is UByte -> 1
            is Short, is UShort -> 2
            is Int, is UInt -> 4
            is Long, is ULong -> 8
            else -> throw IllegalArgumentException("Unrecognized number type: ${value.javaClass}")
        }
        leWrite(value, startAt + (i * length))
    }
}

/**
 * Write the bytes of a number into this [ByteArray], in little-endian order.
 */
fun ByteArray.leWrite(value: Any, index: Int) {
    when (value) {
        is Byte -> this[index] = value
        is UByte -> this[index] = value.toByte()
        is Short -> value.toLittleEndian().copyInto(this, index)
        is UShort -> value.toShort().toLittleEndian().copyInto(this, index)
        is Int -> value.toLittleEndian().copyInto(this, index)
        is UInt -> value.toInt().toLittleEndian().copyInto(this, index)
        is Long -> value.toLittleEndian().copyInto(this, index)
        is ULong -> value.toLong().toLittleEndian().copyInto(this, index)
        else -> throw IllegalArgumentException("Unrecognized number: ${value.javaClass}")
    }
}