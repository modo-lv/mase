package gui.controllers

import Main
import gui.models.MainModel
import gui.models.SaveFileModel
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.TabPane
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import ktfx.bindings.stringBindingBy
import utils.Adom

class MainController {
    lateinit var mainScene: Scene
    lateinit var saveMenuItem: MenuItem
    lateinit var closeMenuItem: MenuItem
    lateinit var mainTabs: TabPane
    lateinit var signature: Label
    lateinit var footer: HBox
    lateinit var footerFileName: TextField

    lateinit var model: MainModel

    private lateinit var fileChooser: FileChooser

    // Called by JavaFX on init
    fun initialize() {
        model = MainModel().initialize()

        saveMenuItem.disableProperty().bind(Main.SaveProperty.isNull)
        closeMenuItem.disableProperty().bind(Main.SaveProperty.isNull)

        signature.textProperty().bind(model.signature)

        mainTabs.disableProperty().bind(Main.SaveProperty.isNull)

        footer.disableProperty().bind(Main.SaveProperty.isNull)
        footerFileName.textProperty().bind(Main.SaveProperty.stringBindingBy { it?.file?.absolutePath ?: "" })

        Main.SaveProperty.addListener { _, _, _ ->
            model.initialize()
        }
    }

    fun open() {
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

    fun save() {
        Main.Save?.apply {
            fixChecksums()
            file.writeBytes(bytes)
        }
    }

    fun close() {
        Main.Save = null
    }
}