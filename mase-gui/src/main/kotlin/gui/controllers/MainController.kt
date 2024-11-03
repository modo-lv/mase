package gui.controllers

import gui.models.MainModel
import gui.models.SaveFileModel
import javafx.scene.Scene
import javafx.scene.control.TabPane
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import java.io.File

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
                initialDirectory = File("c:\\users\\martin\\Documents\\ADOM\\adom_steam\\savedg")
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