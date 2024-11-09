package models.factories

import Main
import javafx.collections.ObservableList
import models.PlayerSkill
import models.values.SkillValue

object SkillValueFactory {
    /**
     * Replace the contents of [skillList] with values loaded from save data.
     */
    fun putAll(skillList: ObservableList<SkillValue>) {
        skillList.clear()
        if (Main.Save == null) return

        Main.Save!!.player.skills.readSkills(includeMissing = true)
            .sortedWith(compareByDescending<PlayerSkill> { it.acquired }.thenBy { it.name })
            .forEach { skill ->
                skillList.add(
                    SkillValue(
                        underlying = skill
                    )
                )
            }
    }
}