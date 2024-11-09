package controllers.tabs

import content.Profession
import controllers.MainController
import controllers.editors.EditorContainerController
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import ktfx.bindings.stringBindingOf
import ktfx.controls.find
import models.MainModel
import models.values.SkillValue
import utils.toDisplayString

class SkillTabController {
    lateinit var skillTable: TableView<SkillValue>

    lateinit var name: TableColumn<SkillValue, String>
    lateinit var level: TableColumn<SkillValue, Byte>
    lateinit var inactive: TableColumn<SkillValue, Int>
    lateinit var active: TableColumn<SkillValue, Int>
    lateinit var advancementLevel: TableColumn<SkillValue, Byte>
    lateinit var advancementDice: TableColumn<SkillValue, String>
    lateinit var max: TableColumn<SkillValue, String>


    fun initialize() {
        skillTable.apply {
            items = MainModel.skills
            setRowFactory {
                object : TableRow<SkillValue>() {
                    override fun updateItem(item: SkillValue?, empty: Boolean) {
                        super.updateItem(item, empty)
                        styleClass.remove("unavailable")
                        if (item?.isAvailable != true) {
                            styleClass.add("unavailable")
                        }
                    }
                }.apply {
                    setOnMouseClicked { if (it.clickCount == 2) openEditor(item) }
                }
            }
            setOnKeyPressed { if (it.code == KeyCode.ENTER) openEditor(selectionModel.selectedItem) }
        }

        name.setCellValueFactory { cell -> cell.value.nameProperty }
        level.setCellValueFactory { cell -> cell.value.levelProperty }
        inactive.setCellValueFactory { cell -> cell.value.inactiveTrainingProperty }
        active.setCellValueFactory { cell -> cell.value.activeTrainingProperty }
        advancementLevel.setCellValueFactory { cell -> cell.value.advancementLevelProperty }
        advancementDice.setCellValueFactory { cell ->
            // TODO: Profession change binding
            stringBindingOf(cell.value.advancementLevelProperty, cell.value.levelProperty) {
                "[+${cell.value.underlying.advancementDice(Profession.Ranger)}]"
            }
        }
        max.setCellValueFactory { cell ->
            // TODO: Profession change binding
            stringBindingOf(cell.value.levelProperty, cell.value.advancementLevelProperty, cell.value.isAvailableProperty) {
                if (cell.value.isAvailable)
                    cell.value.underlying.maxLevel(Profession.Ranger).toDisplayString()
                else
                    ""
            }
        }

        Platform.runLater {
            val x = skillTable.find<Node>(".table-cell.computed")
            println(x)
        }
    }

    private fun openEditor(item: SkillValue) {
        MainController.openEditor(
            table = skillTable,
            controller = EditorContainerController(item)
        )
    }

}