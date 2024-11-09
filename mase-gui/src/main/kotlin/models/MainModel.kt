package models

import Main
import content.Profession
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import ktfx.collections.toMutableObservableList
import models.factories.SkillValueFactory
import models.factories.StatValueFactory
import models.values.GameValue
import models.values.SkillValue
import utils.ObservableDelegates.delegateTo

object MainModel {
    val stats = mutableListOf<GameValue<out Number>>().toMutableObservableList()
    val skills = mutableListOf<SkillValue>().toMutableObservableList()

    val professionProperty = SimpleObjectProperty<Profession>(null)
    val profession: Profession by delegateTo(professionProperty)

    var signature = SimpleStringProperty(null)

    init {
        Main.SaveProperty.addListener { _, _, _ -> reload() }
        reload()
    }

    fun reload() {
        signature.set(null)
        if (Main.Save == null)
            return
        signature.set(Main.Save!!.player.character.signature)
        StatValueFactory.putAll(stats)
        SkillValueFactory.putAll(skills)
    }
}