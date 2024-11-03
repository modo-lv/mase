package gui.models

import Main
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import ktfx.collections.toMutableObservableList

class MainModel {
    var signature = SimpleStringProperty(null)
    val attributes = mutableListOf<StatModel<*>>().toMutableObservableList()

    fun initialize(): MainModel {
        signature.set(null)
        attributes.clear()

        if (Main.Save == null)
            return this

        signature.set(Main.Save!!.player.character.signature)
        attributes.add(
            StatModel(
                name = "Experience points",
                currentValue = SimpleObjectProperty(Main.Save!!.player.character.xp)
            )
        )

        return this
    }
}