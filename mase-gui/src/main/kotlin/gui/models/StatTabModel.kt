package gui.models

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
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
                name = "Level",
                currentValue = SimpleObjectProperty(Main.Save!!.player.character.level),
                _cc = { Main.Save!!.player.character.level = it.currentValue.value }
            )
        )

        attributes.add(
            StatModel(
                name = "Experience points",
                currentValue = SimpleLongProperty(Main.Save!!.player.character.xp),
                _cc = { Main.Save!!.player.character.xp = it.currentValue.value.toLong() }
            )
        )

        attributes.add(
            StatModel(
                name = "Alignment",
                currentValue = SimpleIntegerProperty(Main.Save!!.player.character.alignment),
                _cc = { Main.Save!!.player.character.alignment = it.currentValue.value.toInt() }
            )
        )

        return this
    }
}