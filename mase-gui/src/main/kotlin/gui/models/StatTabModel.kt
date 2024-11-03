package gui.models

import javafx.beans.property.SimpleObjectProperty
import ktfx.collections.toMutableObservableList

class StatTabModel {
    val attributes = mutableListOf<StatModel<*>>().toMutableObservableList()

    fun initialize(): StatTabModel {
        attributes.clear()

        if (Main.Save == null)
            return this

        attributes.add(
            StatModel(
                name = "Experience points",
                currentValue = SimpleObjectProperty(Main.Save!!.player.character.xp),
                _cc = { Main.Save!!.player.character.xp = it.currentValue.value }
            )
        )

        return this
    }
}