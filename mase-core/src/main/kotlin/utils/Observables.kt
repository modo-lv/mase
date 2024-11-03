package utils

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import kotlin.reflect.KProperty

object Observables {
    fun doubleProperty(property: DoubleProperty) =
        ObservableDoubleProperty(property)

    fun <T> objectProperty(property: ObjectProperty<T>) =
        ObservableObjectProperty(property)


    class ObservableDoubleProperty(val realProp: DoubleProperty) {
        operator fun getValue(thisRef: Any, property: KProperty<*>): Double = realProp.get()
        operator fun setValue(thisRef: Any, property: KProperty<*>, value: Double) = realProp.set(value)
    }

    class ObservableObjectProperty<T>(val realProp: ObjectProperty<T>) {
        operator fun getValue(thisRef: Any, property: KProperty<*>): T = realProp.get()
        operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) = realProp.set(value)
    }
}