package models

import Main
import content.Profession
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import ktfx.collections.mutableObservableListOf
import ktfx.collections.toMutableObservableList
import models.factories.SkillValueFactory
import models.factories.StatValueFactory
import models.values.GameValue
import models.values.SkillValue
import utils.ObservableDelegates.delegateTo
import java.io.File

object MainModel {
    val stats = mutableListOf<GameValue<out Number>>().toMutableObservableList()
    val skills = mutableListOf<SkillValue>().toMutableObservableList()

    val professionProperty = SimpleObjectProperty<Profession>(null)
    var profession: Profession? by delegateTo(professionProperty)

    val directoryProperty = SimpleObjectProperty<File>(null)
    var directory: File? by delegateTo(directoryProperty)

    val saveListProperty = SimpleListProperty<SaveGame>(mutableObservableListOf())
    var saveList: ObservableList<SaveGame> by delegateTo(saveListProperty)

    init {
        Main.SaveProperty.addListener { _, _, _ -> reload() }
        reload()
    }

    fun reload() {
        profession = null
        if (Main.Save == null)
            return

        profession = Main.Save!!.player.character.profession
        StatValueFactory.putAll(stats)
        SkillValueFactory.putAll(skills)

        Main.Save!!.file.parentFile!!.also { newDir ->
            if (directory?.absolutePath != newDir.absolutePath) {
                saveList.clear()
                for (file in newDir.listFiles { _, name -> name.lowercase().endsWith(".svg") }!!) {
                    saveList.add(SaveGame(file, SaveFileModel(file).player.character.signature))
                }
            }
            directory = newDir
        }
    }


    class SaveGame(val file: File, val signature: String) {
        override fun toString(): String {
            return "${signature} (${file.name})"
        }
    }
}