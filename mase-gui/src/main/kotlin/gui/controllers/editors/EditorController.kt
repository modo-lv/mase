package gui.controllers.editors

import gui.models.StatModel
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableValue

interface EditorController {
    val model: StatModel<*>
}