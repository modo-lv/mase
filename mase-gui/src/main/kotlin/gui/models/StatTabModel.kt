package gui.models

import javafx.beans.property.SimpleObjectProperty
import ktfx.collections.toMutableObservableList

class StatTabModel {
    val attributes = mutableListOf<StatModel<*>>().toMutableObservableList()

    fun initialize(): StatTabModel {
        attributes.clear()

        attributes.add(
            StatModel(
                name = "Experience points",
                currentValue = SimpleObjectProperty(Main.Save!!.player.character.xp)
            )
        )

        return this
    }
}