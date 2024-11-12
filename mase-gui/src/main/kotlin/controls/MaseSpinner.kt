package controls

import javafx.application.Platform
import javafx.scene.control.Spinner

class MaseSpinner<T> : Spinner<T>() {
    init {
        focusedProperty().addListener { _, _, focused ->
            if (focused) Platform.runLater { editor.selectAll() }
        }
    }
}