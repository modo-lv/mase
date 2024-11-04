package gui.controllers

import gui.controllers.editors.SingleNumberEditorController
import gui.models.StatModel
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import ktfx.windows.stage

class EditorController(val model: StatModel<*>) {
    lateinit var editorScene: Scene
    lateinit var region: StackPane
    lateinit var statLabel: Label

    fun initialize() {
        statLabel.text = model.name

        val editor = FXMLLoader(javaClass.getResource("/gui/editors/single-number.fxml")).run {
            this.setController(SingleNumberEditorController(model as StatModel<Number>))
            load<Node>()
        }
        region.children.add(editor)
    }

    fun ok(event: ActionEvent) {
        model.commitChanges()
        (event.target as Node).scene.stage.close()
    }

    fun cancel(event: ActionEvent) {
        (event.target as Node).scene.stage.close()
    }
}