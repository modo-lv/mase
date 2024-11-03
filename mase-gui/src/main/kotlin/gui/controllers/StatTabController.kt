package gui.controllers

import gui.models.StatModel
import gui.models.StatTabModel
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView

class StatTabController {
    lateinit var statTable: TableView<StatModel<*>>
    lateinit var statCurrent: TableColumn<StatModel<Any?>, Any?>

    private lateinit var model: StatTabModel

    fun initialize() {
        model = StatTabModel().initialize()

        statTable.items = model.attributes

        statCurrent.setCellValueFactory { cell ->
            cell.value.currentValue
        }
    }
}