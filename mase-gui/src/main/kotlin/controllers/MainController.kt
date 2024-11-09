package controllers

import Main
import javafx.fxml.FXMLLoader
import models.MainModel
import models.SaveFileModel
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
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

    private lateinit var fileChooser: FileChooser

    // Called by JavaFX on init
    fun initialize() {
        saveMenuItem.disableProperty().bind(Main.SaveProperty.isNull)
        closeMenuItem.disableProperty().bind(Main.SaveProperty.isNull)

        signature.textProperty().bind(MainModel.signature)

        mainTabs.disableProperty().bind(Main.SaveProperty.isNull)

        footer.disableProperty().bind(Main.SaveProperty.isNull)
        footerFileName.textProperty().bind(Main.SaveProperty.stringBindingBy { it?.file?.absolutePath ?: "" })
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

    companion object {
        fun openEditor(table: TableView<*>, controller: Any) {
            Stage(StageStyle.UTILITY).apply {
                scene = FXMLLoader(javaClass.getResource("/gui/editor.fxml")).run {
                    setController(controller)
                    load<Scene>().apply {
                        stylesheets.add("/gui/style.css")
                    }
                }
                initModality(Modality.APPLICATION_MODAL)
                showAndWait()
                table.selectionModel.selectedIndex.also {
                    MainModel.reload()
                    table.selectionModel.select(it)
                }
            }
        }
    }
}