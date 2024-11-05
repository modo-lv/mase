package gui.components.spinner

import com.sun.javafx.util.Utils
import javafx.scene.control.SpinnerValueFactory
import ktfx.text.StringConverterBuilder
import kotlin.math.abs

class NumericSpinnerValueFactory(
    val min: Long = Long.MIN_VALUE,
    val max: Long = Long.MAX_VALUE,
) : SpinnerValueFactory<Number>() {
    constructor(min: Short, max: Short) : this(min.toLong(), max.toLong())
    constructor(min: Int, max: Int) : this(min.toLong(), max.toLong())

    init {
        converter = StringConverterBuilder<Number>().run {
            fromString { it.toLong() }
            toString { it.toString() }
            build()
        }
    }

    override fun decrement(steps: Int) {
        val newValue = value.toLong() - steps.toLong()
        value =
            if (newValue < min || newValue > max)
                wrap(newValue)
            else
                Utils.clamp(newValue, min, max)
    }

    override fun increment(steps: Int) {
        val newValue = value.toLong() + steps.toLong()
        value =
            if (newValue < min || newValue > max)
                wrap(newValue)
            else
                Utils.clamp(newValue, min, max)
    }

    fun wrap(value: Long): Long {
        val (limit, opposite) =
            if (value < min) (min to max)
            else if (value > max) (max to min)
            else return value
        return opposite + ((value - limit) % (max - min + 1)).let { if (abs(it) == 1L) 0 else it }
    }
}