package gui

import Main
import javafx.beans.property.SimpleObjectProperty

object Model {
    private val _xp = SimpleObjectProperty<Double>(null)
    var xp: Double
        get() = _xp.get()
        set(value) = _xp.set(value)
    val xpProperty get() = _xp


    fun initialize() {
        xp = Main.Save.player.character.xp.toDouble()
        xpProperty.addListener { _, _, new ->
            Main.Save.player.character.xp = new.toULong()
        }
    }
}