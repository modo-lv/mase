package utils

import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import kotlin.reflect.KProperty

object ObservableDelegates {
    /**
     * Delegate value read/write to another, observable property.
     */
    fun <T> delegateTo(property: Property<T>) = ObservablePropertyDelegate(property)
    class ObservablePropertyDelegate<T>(val realProp: Property<T>) {
        operator fun getValue(thisRef: Any, property: KProperty<*>): T = realProp.value
        operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            realProp.value = value
        }
    }

    /**
     * Delegate value read to another, observable value.
     */
    fun <T> delegateTo(property: ObservableValue<T>) = ObservableValueDelegate(property)
    class ObservableValueDelegate<T>(val realProp: ObservableValue<T>) {
        operator fun getValue(thisRef: Any, property: KProperty<*>): T = realProp.value
    }
}