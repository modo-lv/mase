package gui.controllers.editors

import gui.models.StatModel
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory

class SingleNumberEditorController(override val model: StatModel<Number>) : EditorController {
    lateinit var input: Spinner<Number>

    fun initialize() {
        input.valueFactory = when (model.currentValue.value) {
            is Int -> IntSpinner
            is Long -> LongSpinner
            else -> throw IllegalArgumentException()
        } as SpinnerValueFactory<Number>

        input.valueFactory.valueProperty().bindBidirectional(model.currentValue)
    }

    companion object {
        val IntSpinner = IntegerSpinnerValueFactory(Int.MIN_VALUE, Int.MAX_VALUE)
        val LongSpinner = object : SpinnerValueFactory<Long>() {
            override fun decrement(steps: Int) = valueProperty().set(valueProperty().get() - steps)
            override fun increment(steps: Int) = valueProperty().set(valueProperty().get() + steps)
        }
    }
}