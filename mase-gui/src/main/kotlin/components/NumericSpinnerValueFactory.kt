package components

import javafx.scene.control.SpinnerValueFactory
import ktfx.text.StringConverterBuilder

class NumericSpinnerValueFactory(
    val min: Long = Long.MIN_VALUE,
    val max: Long = Long.MAX_VALUE,
) : SpinnerValueFactory<Number>() {
    constructor(min: Byte, max: Byte) : this(min.toLong(), max.toLong())
    constructor(min: Short, max: Short) : this(min.toLong(), max.toLong())
    constructor(min: Int, max: Int) : this(min.toLong(), max.toLong())

    init {
        converter = StringConverterBuilder<Number>().run {
            fromString { limit(it.toLong()) }
            toString { it.toString() }
            build()
        }
    }

    override fun decrement(steps: Int) {
        value = limit(value.toLong() - steps.toLong())
    }

    override fun increment(steps: Int) {
        value = limit(value.toLong() + steps.toLong())
    }


    fun limit(value: Long): Long {
        return when (value) {
            min - 1 -> max
            max + 1 -> min
            else -> value.coerceIn(min .. max)
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T> createFor(value: Number): SpinnerValueFactory<T> = when (value) {
            is Byte -> NumericSpinnerValueFactory(Byte.MIN_VALUE.toLong(), Byte.MAX_VALUE.toLong())
            is Short -> NumericSpinnerValueFactory(Short.MIN_VALUE, Short.MAX_VALUE)
            is Int -> NumericSpinnerValueFactory(Int.MIN_VALUE, Int.MAX_VALUE)
            is Long -> NumericSpinnerValueFactory()
            else -> throw IllegalArgumentException(
                "Can't create spinner value factory for [${value::class.simpleName}]"
            )
        } as SpinnerValueFactory<T>
    }
}