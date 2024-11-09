package controllers

import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import ktfx.bindings.stringBindingBy
import ktfx.bindings.stringBindingOf
import models.MainModel
import models.values.SkillValue

class SkillTabController {
    lateinit var skillTable: TableView<SkillValue>

    lateinit var name: TableColumn<SkillValue, String>
    lateinit var level: TableColumn<SkillValue, Byte>
    lateinit var practical: TableColumn<SkillValue, Int>
    lateinit var theoretical: TableColumn<SkillValue, Byte>
    lateinit var limit: TableColumn<SkillValue, Int>


    fun initialize() {
        skillTable.apply {
            items = MainModel.skills
        }

        name.setCellValueFactory { cell -> cell.value.nameProperty }
        level.setCellValueFactory { cell -> cell.value.levelProperty }
        practical.setCellValueFactory { cell -> cell.value.practicalProperty }
        theoretical.setCellValueFactory { cell -> cell.value.theoreticalProperty }
        limit.setCellValueFactory { cell -> cell.value.limitProperty }
    }
}