package models

import components.SkillAdvancement
import content.Profession
import content.Skill
import content.WithIntId
import models.PlayerSkill.State.Available
import utils.toSentenceString

/**
 * Player skill data in the save data.
 *
 * @param type Which skill the data is for.
 */
data class PlayerSkill(
    val type: Skill,
    var state: State,
    var level: Byte,
    var advancement: Byte,
    var activeTraining: Int,
    var inactiveTraining: Int,
) {
    val id = type.id
    val name = type.toSentenceString()

    val isAvailable get() = type != Skill.Nothing && state == Available && level > 0

    enum class State(override val id: Int): WithIntId {
        NotAvailable(0),
        Available(1),
    }
}