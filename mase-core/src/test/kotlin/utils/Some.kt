package utils

import content.Skill
import models.PlayerSkill
import models.PlayerSkill.State.Available

object Some {
    fun playerSkill(type: Skill) = PlayerSkill(
        type = type,
        state = Available,
        level = 1,
        advancement = 0,
        activeTraining = 0,
        inactiveTraining = 0,
    )
}