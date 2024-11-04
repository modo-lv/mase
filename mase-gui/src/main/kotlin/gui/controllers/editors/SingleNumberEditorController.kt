package gui.controllers.editors

import gui.models.StatModel
import javafx.application.Platform
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory
import ktfx.text.StringConverterBuilder

class SingleNumberEditorController(override val model: StatModel<Number>) : EditorController {
    lateinit var input: Spinner<Number>

    fun initialize() {
        input.apply {
            valueFactory = when (model.currentValue.value) {
                is Int -> IntSpinner
                is Long -> LongSpinner
                else -> throw IllegalArgumentException()
            } as SpinnerValueFactory<Number>
            valueFactory.valueProperty().bindBidirectional(model.currentValue)
            Platform.runLater { requestFocus(); editor.selectAll() }
        }
    }

    companion object {
        val IntSpinner = IntegerSpinnerValueFactory(Int.MIN_VALUE, Int.MAX_VALUE)
        val LongSpinner = object : SpinnerValueFactory<Long>() {
            init {
                converter = StringConverterBuilder<Long>().run {
                    fromString { it.toLong() }
                    toString { it.toString() }
                    build()
                }
            }
            override fun decrement(steps: Int) = valueProperty().set(valueProperty().get() - steps)
            override fun increment(steps: Int) = valueProperty().set(valueProperty().get() + steps)
        }
    }
}