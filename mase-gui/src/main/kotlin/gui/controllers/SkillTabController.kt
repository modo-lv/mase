package gui.controllers

import javafx.scene.control.TableColumn
import javafx.scene.control.TableView

class SkillTabController {
    lateinit var skillTable: TableView<Any>

    lateinit var name: TableColumn<Any, Any>
    lateinit var level: TableColumn<Any, Any>
    lateinit var practical: TableColumn<Any, Any>
    lateinit var theoretical: TableColumn<Any, Any>
    lateinit var limit: TableColumn<Any, Any>


    fun initialize() {

    }
}