package components

import javafx.scene.control.SpinnerValueFactory
import ktfx.text.StringConverterBuilder

class NumericSpinnerValueFactory(
    val min: Long = Long.MIN_VALUE,
    val max: Long = Long.MAX_VALUE,
) : SpinnerValueFactory<Number>() {
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
            else -> value.coerceIn(min..max)
        }
    }
}