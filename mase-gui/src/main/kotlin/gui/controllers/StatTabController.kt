package gui.controllers

import gui.models.StatModel
import gui.models.StatTabModel
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle

class StatTabController {
    lateinit var statTable: TableView<StatModel<*>>
    lateinit var statValue: TableColumn<StatModel<Any>, Any>

    private lateinit var model: StatTabModel

    fun initialize() {
        model = StatTabModel().initialize()

        statTable.apply {
            items = model.attributes
            setRowFactory {
                TableRow<StatModel<*>>().apply {
                    setOnMouseClicked { if (it.clickCount == 2) openEditor(item) }
                }
            }
            setOnKeyPressed { if (it.code == KeyCode.ENTER) openEditor(selectionModel.selectedItem) }
        }

        statValue.setCellValueFactory { it.value.currentValue }

        Main.SaveProperty.addListener { _, _, _ ->
            model.initialize()
        }
    }

    private fun openEditor(model: StatModel<*>) {
        Stage(StageStyle.UTILITY).apply {
            scene = FXMLLoader(javaClass.getResource("/gui/editor.fxml")).run {
                @Suppress("UNCHECKED_CAST")
                setController(EditorController(model as StatModel<Any>))
                load<Scene>().apply {
                    stylesheets.add("/gui/style.css")
                }
            }
            initModality(Modality.APPLICATION_MODAL)
            showAndWait()
            statTable.selectionModel.selectedIndex.also {
                refresh()
                statTable.selectionModel.select(it)
            }
        }
    }

    fun refresh() {
        model.initialize()
    }
}