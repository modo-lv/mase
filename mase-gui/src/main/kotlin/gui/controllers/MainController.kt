package gui.controllers

import Main
import gui.models.SaveFileModel
import javafx.scene.Scene
import javafx.scene.control.TabPane
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import utils.Adom

class MainController {
    lateinit var mainScene: Scene
    lateinit var mainTabs: TabPane

    private lateinit var fileChooser: FileChooser

    // Called by JavaFX on init
    fun initialize() {
        mainTabs.disableProperty().bind(Main.SaveProperty.isNull)
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