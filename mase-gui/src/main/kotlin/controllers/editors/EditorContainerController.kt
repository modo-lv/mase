package controllers.editors

import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import ktfx.windows.stage
import models.values.GameValue
import models.values.SkillValue
import models.values.ValueModel
import java.lang.IllegalArgumentException

class EditorContainerController<T : ValueModel>(val model: T) {
    lateinit var editorScene: Scene
    lateinit var region: StackPane
    lateinit var statLabel: Label

    fun initialize() {
        statLabel.text = model.name

        val loader = when (model) {
            is GameValue<*> ->
                FXMLLoader(javaClass.getResource("/gui/editors/single-number.fxml")).apply {
                    @Suppress("UNCHECKED_CAST")
                    this.setController(SingleNumberEditorController(model as GameValue<Number>))
                }
            is SkillValue ->
                FXMLLoader(javaClass.getResource("/gui/editors/skill.fxml")).apply {
                    this.setController(SkillEditorController(model))
                }

            else -> throw IllegalArgumentException("Unknown model type [${model::class.simpleName}].")
        }
        val editor = loader.load<Node>()

        region.children.add(editor)
    }

    fun ok(event: ActionEvent) {
        model.commit()
        (event.target as Node).scene.stage.close()
    }

    fun cancel(event: ActionEvent) {
        (event.target as Node).scene.stage.close()
    }
}