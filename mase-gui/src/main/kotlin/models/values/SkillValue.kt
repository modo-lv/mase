package models.values

import SaveData
import components.SkillAdvancement
import content.Profession
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import ktfx.bindings.booleanBindingOf
import models.PlayerSkill
import models.PlayerSkill.State.Available
import utils.ObservableDelegates.delegateTo

class SkillValue(
    val save: SaveData<*>,
    val underlying: PlayerSkill,
) : ValueModel {
    val nameProperty = SimpleStringProperty(underlying.name)
    override val name: String by delegateTo(nameProperty)

    val stateProperty = SimpleObjectProperty(underlying.state)
    var state: PlayerSkill.State by delegateTo(stateProperty)

    val levelProperty = SimpleObjectProperty(underlying.level)
    var level: Byte by delegateTo(levelProperty)

    val inactiveTrainingProperty = SimpleObjectProperty(underlying.inactiveTraining)
    var inactiveTraining: Int by delegateTo(inactiveTrainingProperty)

    val activeTrainingProperty = SimpleObjectProperty(underlying.activeTraining)
    var activeTraining: Int by delegateTo(activeTrainingProperty)

    val advancementLevelProperty = SimpleObjectProperty(underlying.advancement)
    var advancementLevel: Byte by delegateTo(advancementLevelProperty)

    val isAvailableProperty = SimpleBooleanProperty(underlying.isAvailable)
    val isAvailable: Boolean by delegateTo(isAvailableProperty)

    fun toPlayerSkill() = PlayerSkill(
        type = underlying.type,
        state = state,
        level = level,
        advancement = advancementLevel,
        activeTraining = activeTraining,
        inactiveTraining = inactiveTraining,
    )

    fun advancementDice(profession: Profession) =
        SkillAdvancement.computeDice(profession, this.toPlayerSkill())

    fun maxLevel(profession: Profession): IntRange =
        SkillAdvancement.maxLevel(profession, this.toPlayerSkill())

    override fun commit() {
        underlying.state = state
        underlying.level = level
        underlying.inactiveTraining = inactiveTraining
        underlying.advancement = advancementLevel
        underlying.activeTraining = activeTraining

        save.player.skills.writeSkill(underlying)
    }
}