package utils

import javafx.beans.value.WritableValue
import kotlin.reflect.KProperty

object ObservableDelegates {
    /**
     * Delegate value read/write to another (usually private), observable property.
     */
    fun <T> delegateTo(property: WritableValue<T>) = ObservableValuePropertyDelegate(property)

    class ObservableValuePropertyDelegate<T>(val realProp: WritableValue<T>) {
        operator fun getValue(thisRef: Any, property: KProperty<*>): T = realProp.value
        operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            realProp.value = value
        }
    }
}