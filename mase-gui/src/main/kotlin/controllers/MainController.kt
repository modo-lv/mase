package controllers

import Main
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
            selectionModel.selectedItemProperty().addListener { _, _, new -> switchSave(new.file) }
        }

        Main.SaveProperty.addListener { _, _, _ -> saveChanged() }

        mainTabs.disableProperty().bind(Main.SaveProperty.isNull)

        footer.disableProperty().bind(Main.SaveProperty.isNull)
        footerFileName.textProperty().bind(Main.SaveProperty.stringBindingBy { it?.file?.absolutePath ?: "" })
    }

    fun saveChanged() {
        Main.Stage.title = "MASE"
        Main.Save?.also { save ->
            saveSelector.apply { selectionModel.select(items.indexOfFirst { it.file == save.file }) }
            Main.Stage.title = "${save.file.name} - MASE"
        }
    }

    fun switchSave(newFile: File) {
        if (Main.Save?.file != newFile) {
            var doSwitch = true
            val changes =
                Main.Save?.apply { computeChecksums() }?.checksumSegments?.any { it.isMismatched() } == true
            if (changes) {
                doSwitch = Alert(
                    Alert.AlertType.WARNING,
                    "Unsaved changes will be discarded if you switch to another save!",
                    ButtonType.OK, ButtonType.CANCEL
                ).run {
                    showAndWait()
                    result == ButtonType.OK
                }
            }

            if (doSwitch)
                Main.Save = SaveFileModel(newFile)
            else saveChanged() // Save hasn't changed, but this will reset any save-related selections
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
        fileChooser.showOpenDialog(mainScene.window)?.also(::switchSave)
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
                scene = FXMLLoader(javaClass.getResource("/gui/editor-window.fxml")).run {
                    setController(controller)
                    load<Scene>().apply {
                        stylesheets.add("/gui/style.css")
                    }
                }
                initModality(Modality.APPLICATION_MODAL)
                initOwner(Main.Stage)
                showAndWait()
                table.selectionModel.selectedIndex.also {
                    MainModel.reload()
                    table.selectionModel.select(it)
                }
            }
        }
    }
}