import content.Skill
import models.PlayerSkill
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

class SkillTests {
    val known = listOf(
        PlayerSkill(type = Skill.Alertness, level = 10, trainingLevel = 8, maxDelta = 0),
        PlayerSkill(type = Skill.Climbing, level = 32, trainingLevel = 2, maxDelta = 0),
        PlayerSkill(type = Skill.Concentration, level = 59, trainingLevel = 9, maxDelta = 0),
        PlayerSkill(type = Skill.Dodge, level = 12, trainingLevel = 3, maxDelta = 0),
        PlayerSkill(type = Skill.FirstAid, level = 29, trainingLevel = 3, maxDelta = 0),
        PlayerSkill(type = Skill.Gemology, level = 24, trainingLevel = 7, maxDelta = 0)
    ).associateBy { it.type }

    @Test fun `Single skills read correctly`() {
        Resources.testSave().player.skills.apply {
            known.keys.forEach { key ->
                readSkill(key) `should be equal to` known[key]
            }
        }
    }

    @Test fun `Skill list reads correctly`() {
        Resources.testSave().player.skills.readSkills().apply {
            get(0) `should be equal to` known[Skill.Alertness]
            get(1) `should be equal to` known[Skill.Climbing]
            get(2) `should be equal to` known[Skill.Concentration]
            get(3) `should be equal to` known[Skill.Dodge]
            get(4) `should be equal to` known[Skill.FirstAid]
            get(5) `should be equal to` known[Skill.Gemology]
        }
    }

    @Test fun `Existing skills update correctly`() {
        Resources.testSave().player.skills.apply {
            val updated = known[Skill.Alertness]!!.let { it.copy(level = (it.level + 10).toByte()) }
            writeSkill(updated)
            readSkills() `should contain` updated
        }
    }

    @Test fun `New skill is added correctly`() {
        Resources.testSave().player.skills.apply {
            val new = PlayerSkill(type = Skill.DetectItemStatus, level = 10, trainingLevel = 0, maxDelta = 0)
            writeSkill(new)
            readSkills() `should contain` new
        }
    }
}