package gui.models

import javafx.beans.property.Property

class StatModel<T>(
    val name: String,
    val currentValue: Property<T>,
    private val _cc: (StatModel<T>) -> Unit,
) {
    fun commitChanges() = _cc(this)
}