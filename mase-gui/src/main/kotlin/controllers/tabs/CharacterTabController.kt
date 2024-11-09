package controllers.tabs

import controllers.MainController
import controllers.editors.EditorContainerController
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import ktfx.bindings.stringBindingBy
import models.MainModel
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


    private fun openEditor(model: Any) {
        @Suppress("UNCHECKED_CAST")
        MainController.openEditor(
            table = statTable,
            controller = EditorContainerController(model as GameValue<Number>)
        )
    }
}