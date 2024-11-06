package models

import SaveData
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import utils.ObservableDelegates.delegateTo
import utils.equalsBytes
import utils.leNum
import utils.leWrite

/**
 * Represents a value in the saved game.
 *
 * @param save Save data to read from / write to.
 * @param name Name to display this value with.
 * @param address Location of the value in the save data.
 * @param value Initial value.
 * @param TNum What numeric type this value is -- [Byte], [Short], [Int] or [Long].
 */
open class GameValue<TNum : Number>(val save: SaveData<*>, val name: String, val address: Int, value: TNum) {

    val valueProperty = SimpleObjectProperty(value)
    val value: TNum by delegateTo(valueProperty)

    /**
     * Flips to the opposite value to trigger listeners whenever this value is changed in the save game data.
     */
    val commitEventDispatch = SimpleBooleanProperty()


    /**
     * Writes this value to the save data.
     *
     * Does not write to any actual files, only the in-memory byte array of [save].
     */
    protected fun writeData() {
        save.bytes.leWrite(value, address)
    }

    /**
     * Writes this value to the save data byte array and updates [commitEventDispatch].
     */
    fun commit() {
        if (value.equalsBytes(save.bytes, address))
            return
        writeData()
        commitEventDispatch.set(!commitEventDispatch.get())
    }


    companion object {
        /**
         * Creates a [GameValue] instance and loads it up with the data from receiver [SaveData].
         */
        inline fun <reified T : Number> SaveData<*>.gameValue(name: String, address: Int) =
            GameValue(this, name, address, this.bytes.leNum<T>(address))
    }
}