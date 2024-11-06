package gui.models

import Main
import javafx.collections.ObservableList
import models.PlayerCharacter
import models.GameValue
import models.GameValue.Companion.gameValue

object StatValueFactory {
    /**
     * Replace the contents of [statList] with values loaded from save data.
     */
    fun putAll(statList: ObservableList<GameValue<out Number>>) {
        statList.clear()
        Main.Save?.apply {
            statList.add(gameValue<Short>("Level", PlayerCharacter.Addresses.LEVEL))
            statList.add(gameValue<Long>("Experience points", PlayerCharacter.Addresses.XP))
            statList.add(gameValue<Int>("Alignment", PlayerCharacter.Addresses.ALIGNMENT))
        }
    }
}