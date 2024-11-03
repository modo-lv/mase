package gui.controllers.editors

import gui.models.StatModel
import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory

class SingleNumberEditorController(override val model: StatModel<ULong>) : EditorController {
    lateinit var input: Spinner<ULong>

    fun initialize() {
        input.valueFactory = object : SpinnerValueFactory<ULong>() {
            override fun decrement(steps: Int) {
                valueProperty().set(valueProperty().get() - steps.toULong())
            }

            override fun increment(steps: Int) {
                valueProperty().set(valueProperty().get() + steps.toULong())
            }
        }

        input.valueFactory.valueProperty().bindBidirectional(model.currentValue)
    }
}