package utils

import content.WithIntId
import org.kotlincrypto.endians.LittleEndian
import org.kotlincrypto.endians.LittleEndian.Companion.toLittleEndian

/**
 * Compare the bytes (little-endian) of this number to the bytes in a byte array.
 */
fun Number.equalsBytes(other: ByteArray, address: Int = 0): Boolean =
    when (this) {
        is Byte -> this == other[address]

        is Short -> this.toLittleEndian().toByteArray()
            .contentEquals(other.sliceArray(address .. address + 1))

        is Int -> this.toLittleEndian().toByteArray()
            .contentEquals(other.sliceArray(address .. address + 3))

        is Long -> this.toLittleEndian().toByteArray()
            .contentEquals(other.sliceArray(address .. address + 7))

        else -> throw IllegalArgumentException("Unrecognized number type [${this::class.simpleName}].")
    }

/**
 * Read an enum value from this [ByteArray],
 * matching by the [WithIntId.id] property (using little-endian ordering).
 */
inline fun <reified T> ByteArray.leEnum(index: Int): T where T : Enum<T>, T : WithIntId =
    enumValues<T>().single { it.id == this.leNum<Int>(index) }

/**
 * Read a boolean value from ths [ByteArray].
 */
fun ByteArray.boolean(index: Int) = when (this[index]) {
    0.toByte() -> false
    1.toByte() -> true
    else -> throw IllegalArgumentException("Can't convert [${this[index]}] to boolean.")
}

inline fun <reified T: Number> Boolean.toLeNum(): Number = (if (this) 1 else 0).let {
    when (T::class) {
        Byte::class -> it.toByte()
        Short::class -> it.toShort()
        Int::class -> it.toInt()
        Long::class -> it.toLong()
        else -> throw IllegalArgumentException("Unrecognized number type [${T::class.simpleName}]")
    }
}


/**
 * Read a number from this [ByteArray], using little-endian ordering.
 */
inline fun <reified T : Number> ByteArray.leNum(index: Int): T {
    return when (T::class) {
        Short::class ->
            LittleEndian.bytesToShort(this[index], this[index + 1])

        Int::class ->
            LittleEndian.bytesToInt(this[index], this[index + 1], this[index + 2], this[index + 3])

        Long::class -> LittleEndian.bytesToLong(
            this[index + 0], this[index + 1], this[index + 2], this[index + 3],
            this[index + 4], this[index + 5], this[index + 6], this[index + 7],
        )

        else -> throw IllegalArgumentException("Unrecognized number type [${T::class.simpleName}]")
    } as T
}

/**
 * Write the bytes of multiple sequential numbers into this [ByteArray], in little-endian order.
 */
fun ByteArray.leWrite(startAt: Int, values: List<Number>) {
    var address = startAt
    values.forEach { value ->
        leWrite(address, value)
        address += when (value) {
            is Byte -> 1
            is Short -> 2
            is Int -> 4
            is Long -> 8
            else -> throw IllegalArgumentException("Unrecognized number type: ${value.javaClass}")
        }
    }
}

/**
 * Write the bytes of a number into this [ByteArray], in little-endian order.
 */
fun ByteArray.leWrite(index: Int, value: Number) {
    when (value) {
        is Byte -> this[index] = value
        is Short -> value.toLittleEndian().copyInto(this, index)
        is Int -> value.toLittleEndian().copyInto(this, index)
        is Long -> value.toLittleEndian().copyInto(this, index)
        else -> throw IllegalArgumentException("Unrecognized number: ${value.javaClass}")
    }
}