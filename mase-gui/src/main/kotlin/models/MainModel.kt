package models

import Main
import javafx.beans.property.SimpleStringProperty
import ktfx.collections.toMutableObservableList

object MainModel {
    var signature = SimpleStringProperty(null)
    val stats = mutableListOf<GameValue<out Number>>().toMutableObservableList()

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
    }
}