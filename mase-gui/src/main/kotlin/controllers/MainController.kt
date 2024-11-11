package controllers

import Main
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import ktfx.bindings.booleanBindingOf
import ktfx.bindings.stringBindingBy
import models.MainModel
import models.SaveFileModel
import utils.Adom
import java.io.File

class MainController {
    lateinit var mainScene: Scene
    lateinit var saveMenuItem: MenuItem
    lateinit var closeMenuItem: MenuItem
    lateinit var mainTabs: TabPane
    lateinit var saveSelector: ComboBox<MainModel.SaveGame>
    lateinit var footer: HBox
    lateinit var footerFileName: TextField

    private lateinit var fileChooser: FileChooser

    // Called by JavaFX on init
    fun initialize() {
        saveMenuItem.disableProperty().bind(Main.SaveProperty.isNull)
        closeMenuItem.disableProperty().bind(Main.SaveProperty.isNull)

        saveSelector.apply {
            items = MainModel.saveListProperty
            disableProperty().bind(booleanBindingOf(MainModel.saveList) {
                MainModel.saveList.isEmpty()
            })
            selectionModel.selectedItemProperty().addListener { _, _, new ->
                if (Main.Save!!.file.absolutePath != new.file.absolutePath)
                    Main.Save = SaveFileModel(new.file)
            }
        }

        Main.SaveProperty.addListener { _, _, _ ->
            Main.Save?.also { save ->
                saveSelector.apply { selectionModel.select(items.indexOfFirst { it.file == save.file }) }
            }
        }

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
        fileChooser.showOpenDialog(mainScene.window)?.let {
            Main.Save = SaveFileModel(it)
        }
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