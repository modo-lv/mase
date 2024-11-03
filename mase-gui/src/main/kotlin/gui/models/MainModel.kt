package gui.models

import Main
import javafx.beans.property.SimpleStringProperty

class MainModel {
    var signature = SimpleStringProperty(null)

    fun initialize(): MainModel {
        signature.set(null)

        if (Main.Save == null)
            return this

        signature.set(Main.Save!!.player.character.signature)

        return this
    }
}