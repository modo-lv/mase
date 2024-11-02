package gui.tabs

import gui.Model
import javafx.fxml.FXML
import javafx.scene.control.Spinner

open class CharacterController {
    @FXML private lateinit var xpField: Spinner<Double>

    @FXML fun initialize() {
        xpField.valueFactory.valueProperty().bindBidirectional(Model.xpProperty)
    }
}