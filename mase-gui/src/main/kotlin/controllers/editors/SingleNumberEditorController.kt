package controllers.editors

import components.NumericSpinnerValueFactory
import javafx.application.Platform
import javafx.scene.control.Spinner
import models.values.GameValue

class SingleNumberEditorController(val model: GameValue<Number>) {
    lateinit var input: Spinner<Number>

    fun initialize() {
        input.apply {
            valueFactory = NumericSpinnerValueFactory.createFor(model.value)
            valueFactory.valueProperty().bindBidirectional(model.valueProperty)
            Platform.runLater { requestFocus(); editor.selectAll() }
        }
    }
}