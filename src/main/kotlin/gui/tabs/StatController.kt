package gui.tabs

import Main
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Spinner

open class StatController {
    @FXML private lateinit var xpField: Spinner<Double>

    private var initialized: Boolean = false

    @FXML
    protected fun refresh(event: Event) {
        if (!initialized) {
            xpField.valueProperty().addListener { observable, oldValue, newValue ->
                Main.Save.updateXp(newValue.toULong())
                Main.Save.readPlayer()
                xpField.valueFactory.value = Main.Save.xp.toDouble()
            }
            initialized = true
        }
        Main.Save.readPlayer()
        xpField.valueFactory.value = Main.Save.xp.toDouble()

    }
}