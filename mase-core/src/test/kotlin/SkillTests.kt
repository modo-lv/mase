import content.Skill
import models.PlayerSkill.State.NotAvailable
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test
import utils.Some

class SkillTests {
    val known = listOf(
        Some.playerSkill(Skill.Alertness).copy(level = 10, advancement = 8),
        Some.playerSkill(Skill.Climbing).copy(level = 32, advancement = 2),
        Some.playerSkill(Skill.Concentration).copy(level = 59, advancement = 9),
        Some.playerSkill(Skill.Dodge).copy(level = 12, advancement = 3),
        Some.playerSkill(Skill.FirstAid).copy(level = 29, advancement = 3),
        Some.playerSkill(Skill.Gemology).copy(level = 24, advancement = 7),
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
            val updated = known[Skill.Alertness]!!.let {
                it.copy(
                    state = NotAvailable,
                    level = (it.level + 10).toByte()
                )
            }
            writeSkill(updated)
            readSkills() `should contain` updated
        }
    }

    @Test fun `New skill is added correctly`() {
        Resources.testSave().player.skills.apply {
            val new = Some.playerSkill(Skill.DetectItemStatus).copy(
                level = 10,
                advancement = 0,
                inactiveTraining = 0
            )
            writeSkill(new)
            readSkills() `should contain` new
        }
    }
}