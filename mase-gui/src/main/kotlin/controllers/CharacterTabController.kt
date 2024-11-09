package controllers

import models.MainModel
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import ktfx.bindings.stringBindingBy
import models.values.GameValue

class CharacterTabController {
    lateinit var statTable: TableView<GameValue<out Number>>
    lateinit var statValue: TableColumn<GameValue<out Number>, String>

    fun initialize() {
        statTable.items = MainModel.stats

        statTable.apply {
            setRowFactory {
                TableRow<GameValue<out Number>>().apply {
                    setOnMouseClicked { if (it.clickCount == 2) openEditor(item) }
                }
            }
            setOnKeyPressed { if (it.code == KeyCode.ENTER) openEditor(selectionModel.selectedItem) }
        }

        statValue.setCellValueFactory { cell -> cell.value.valueProperty.stringBindingBy { "$it" } }
    }

    private fun openEditor(model: GameValue<*>) {
        Stage(StageStyle.UTILITY).apply {
            scene = FXMLLoader(javaClass.getResource("/gui/editor.fxml")).run {
                @Suppress("UNCHECKED_CAST")
                setController(EditorController(model as GameValue<Number>))
                load<Scene>().apply {
                    stylesheets.add("/gui/style.css")
                }
            }
            initModality(Modality.APPLICATION_MODAL)
            showAndWait()
            statTable.selectionModel.selectedIndex.also {
                MainModel.reload()
                statTable.selectionModel.select(it)
            }
        }
    }
}