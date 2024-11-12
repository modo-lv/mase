package models.factories

import Main
import javafx.collections.ObservableList
import ktfx.bindings.stringBindingOf
import models.MainModel
import models.PlayerSkill
import models.values.SkillValue
import utils.toDisplayString

object SkillValueFactory {
    /**
     * Replace the contents of [skillList] with values loaded from save data.
     */
    fun putAll(skillList: ObservableList<SkillValue>) {
        skillList.clear()
        if (Main.Save == null) return

        Main.Save!!.player.skills.readSkills(includeMissing = true)
            .sortedWith(compareByDescending<PlayerSkill> { it.isAvailable }.thenBy { it.name })
            .forEach { skill ->
                skillList.add(
                    SkillValue(
                        save = Main.Save!!,
                        underlying = skill
                    )
                )
            }
    }

    /**
     * Binding for displaying skill advancement dice for a skill value.
     */
    fun advancementDiceBinding(skill: SkillValue) =
        stringBindingOf(
            skill.advancementLevelProperty, skill.levelProperty, MainModel.professionProperty
        ) {
            if (MainModel.profession != null)
                "[+${skill.advancementDice(MainModel.profession!!)}]"
            else ""
        }

    fun advancementMaxBinding(skill: SkillValue) =
        stringBindingOf(
            skill.levelProperty, skill.advancementLevelProperty, skill.isAvailableProperty,
            skill.activeTrainingProperty, skill.inactiveTrainingProperty,
            MainModel.professionProperty
        ) {
            if (skill.isAvailable && MainModel.profession != null)
                skill.maxLevel(MainModel.profession!!).toDisplayString()
            else ""
        }
}