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

    fun advancementDice(profession: Profession) =
        SkillAdvancement.computeDice(profession, this)

    /**
     * The base max value is [level] + [activeTraining] + [inactiveTraining].
     *
     * However, during level-up, active training is temporarily increased by
     * `(max(diceRoll, diceRoll) / 2) + random(0..3)`
     * (according to http://www.it-is-law.com/adom/index.php?title=Skills),
     * so the actual max value is not exactly predictable.
     */
    fun maxLevel(profession: Profession): IntRange {
        val die = advancementDice(profession)
        val min = (level + activeTraining + inactiveTraining) + die.min
        val max = (level + activeTraining + inactiveTraining) + die.max + 3
        return min.coerceAtMost(100) .. max.coerceAtMost(100)
    }


    enum class State(override val id: Int): WithIntId {
        NotAvailable(0),
        Available(1),
    }
}