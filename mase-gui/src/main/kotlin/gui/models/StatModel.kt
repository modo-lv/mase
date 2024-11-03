package gui.models

import javafx.beans.value.ObservableValue

class StatModel<T>(
    val name: String,
    val currentValue: ObservableValue<T>,
)