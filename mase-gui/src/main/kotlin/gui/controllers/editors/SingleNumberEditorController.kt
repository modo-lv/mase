package gui.controllers.editors

import gui.components.spinner.NumericSpinnerValueFactory
import javafx.application.Platform
import javafx.scene.control.Spinner
import models.GameValue

class SingleNumberEditorController(override val model: GameValue<Number>) : EditorController {
    lateinit var input: Spinner<Number>

    fun initialize() {
        input.apply {
            valueFactory = when (model.value) {
                is Short -> NumericSpinnerValueFactory(Short.MIN_VALUE, Short.MAX_VALUE)
                is Int -> NumericSpinnerValueFactory(Int.MIN_VALUE, Int.MAX_VALUE)
                is Long -> NumericSpinnerValueFactory()
                else -> throw IllegalArgumentException()
            }
            valueFactory.valueProperty().bindBidirectional(model.valueProperty)
            Platform.runLater { requestFocus(); editor.selectAll() }
        }
    }
}