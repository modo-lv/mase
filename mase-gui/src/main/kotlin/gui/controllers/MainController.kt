package gui.controllers

import Main
import gui.models.MainModel
import gui.models.SaveFileModel
import gui.models.StatModel
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.TabPane
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import utils.Adom

class MainController {
    lateinit var mainScene: Scene
    lateinit var mainTabs: TabPane
    lateinit var statTable: TableView<Any>
    lateinit var statCurrent: TableColumn<Any, Any>

    lateinit var model: MainModel

    private lateinit var fileChooser: FileChooser

    // Called by JavaFX on init
    @Suppress("UNCHECKED_CAST")
    fun initialize() {
        model = MainModel().initialize()

        mainTabs.disableProperty().bind(Main.SaveProperty.isNull)

        statTable.items = model.attributes as ObservableList<Any>

        statCurrent.setCellValueFactory { cell ->
            (cell.value as StatModel<Any>).currentValue
        }

        Main.SaveProperty.addListener { _, _, _ ->
            model.initialize()
        }
    }

    fun openFile() {
        if (!this::fileChooser.isInitialized) {
            fileChooser = FileChooser().apply {
                initialDirectory = Adom.defaultSaveFolderPath().toFile()
                title = "Select an ADOM save file to edit"
                extensionFilters.addAll(
                    ExtensionFilter("ADOM save files (*.svg)", "*.svg"),
                    ExtensionFilter("All files", "*"),
                )
            }
        }
        Main.Save = fileChooser.showOpenDialog(mainScene.window)?.let { SaveFileModel(it) }
    }
}