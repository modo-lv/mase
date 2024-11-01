package gui.tabs

import Main
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import structure.Checksum

object Model {
    private val _checksums = FXCollections.observableArrayList<Checksum>()
    var checksums: MutableList<Checksum>
        get() = _checksums
        set(value) {
            _checksums.clear()
            _checksums.addAll(value)
        }
    val checksumsProperty: ObservableList<Checksum> get() = _checksums

    private val _xp = SimpleObjectProperty<Double>(null)
    var xp: Double
        get() = _xp.get()
        set(value) = _xp.set(value)
    val xpProperty get() = _xp


    fun initialize() {
        Main.Save.readPlayer()
        xpProperty.addListener { _, _, new ->
            Main.Save.updateXp(new.toULong())
        }
    }
}