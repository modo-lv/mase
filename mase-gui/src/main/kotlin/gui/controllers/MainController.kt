package gui.controllers

import Main
import gui.models.MainModel
import gui.models.SaveFileModel
import gui.models.StatModel
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import ktfx.bindings.stringBindingBy
import utils.Adom

class MainController {
    lateinit var mainScene: Scene
    lateinit var mainTabs: TabPane
    lateinit var signature: Label
    lateinit var footer: HBox
    lateinit var footerFileName: TextField

    lateinit var model: MainModel

    private lateinit var fileChooser: FileChooser

    // Called by JavaFX on init
    fun initialize() {
        model = MainModel().initialize()

        signature.textProperty().bind(model.signature)

        mainTabs.disableProperty().bind(Main.SaveProperty.isNull)

        footer.disableProperty().bind(Main.SaveProperty.isNull)
        footerFileName.textProperty().bind(Main.SaveProperty.stringBindingBy { it?.file?.absolutePath ?: "" })

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