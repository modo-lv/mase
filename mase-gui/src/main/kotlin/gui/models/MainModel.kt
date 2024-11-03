package gui.models

import Main
import javafx.beans.property.SimpleObjectProperty
import ktfx.collections.toMutableObservableList

class MainModel {
    val attributes = mutableListOf<StatModel<*>>().toMutableObservableList()

    fun initialize(): MainModel {
        attributes.clear()

        if (Main.Save == null)
            return this

        attributes.add(
            StatModel(
                name = "Experience points",
                currentValue = SimpleObjectProperty(Main.Save!!.player.character.xp)
            )
        )

        return this
    }
}