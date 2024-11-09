package models.values

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import ktfx.bindings.bindingOf
import models.PlayerSkill
import utils.ObservableDelegates.delegateTo

class SkillValue(underlying: PlayerSkill) {
    val nameProperty = SimpleStringProperty(underlying.name)
    val name: String by delegateTo(nameProperty)

    val acquiredProperty = SimpleBooleanProperty(underlying.acquired)
    val acquired: Boolean by delegateTo(acquiredProperty)

    val levelProperty = SimpleObjectProperty(underlying.level)
    val level: Byte by delegateTo(levelProperty)

    val practicalProperty = SimpleObjectProperty(underlying.practicalBonus)
    val practical: Int by delegateTo(practicalProperty)

    val theoreticalProperty = SimpleObjectProperty(underlying.theoreticalBonus)
    var theoretical: Byte by delegateTo(theoreticalProperty)

    val limitProperty = bindingOf(levelProperty, practicalProperty, theoreticalProperty) {
        (level + practical + theoretical).coerceIn(0..100)
    }
}