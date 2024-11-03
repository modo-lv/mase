package gui.controllers

import gui.models.StatModel
import gui.models.StatTabModel
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle

class StatTabController {
    lateinit var statTable: TableView<StatModel<*>>
    lateinit var statValue: TableColumn<StatModel<Any>, Any>

    private lateinit var model: StatTabModel

    fun initialize() {
        model = StatTabModel().initialize()

        statTable.items = model.attributes

        statTable.setRowFactory {
            TableRow<StatModel<*>>().also { row ->
                row.setOnMouseClicked { event ->
                    if (event.clickCount == 2) {
                        Stage(StageStyle.UTILITY).apply {
                            scene = FXMLLoader(javaClass.getResource("/gui/editor.fxml")).run {
                                setController(EditorController(row.item))
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
                }
            }
        }

        statValue.setCellValueFactory { it.value.currentValue }

        Main.SaveProperty.addListener { _, _, _ ->
            model.initialize()
        }
    }

    fun refresh() {
        model.initialize()
    }
}