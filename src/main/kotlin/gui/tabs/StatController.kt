package gui.tabs

import javafx.fxml.FXML
import javafx.scene.control.Spinner

open class StatController {
    @FXML private lateinit var xpField: Spinner<Double>

    @FXML fun initialize() {
        xpField.valueFactory.valueProperty().bindBidirectional(Model.xpProperty)
    }
}