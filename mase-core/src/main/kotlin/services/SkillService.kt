package services

import SaveData
import content.Skill
import models.PlayerSkill
import models.PlayerSkill.State.NotAvailable
import structure.Chunk
import utils.*
import java.lang.Exception

/**
 * Reads and writes player skill values to/from save data.
 *
 * Each skill has its own [Chunk.PCSK] chunk, in the [Chunk.GSKD] chunk. They are saved ordered by their IDs.
 *
 * Each PCSK segment content is:
 * 1.  [Int] ID (see [Skill.id])
 * 2.  [Int] 0 for empty slots, 1 for skills player has
 * 3.  [Int] See [PlayerSkill.activeTraining]. Seems to go up in steps of 4, at least for some skills.
 * 4.  [Int] See [PlayerSkill.inactiveTraining].
 * 5. [Byte] Skill level
 * 6. [Byte] See [PlayerSkill.advancement], 1-15.
 *           Affects the maximum level and improvement dice (from [+1] to [+4d5]).
 *           The exact value of the dice for each level depends on the current skill level and profession.
 *           As the skill value goes up, the dice go down. Only level 15 is +4d5 even at 99.
 */
class SkillService(val save: SaveData<*>) {
    val addresses = save.chunks[Chunk.PCSK].takeUnless { it.isNullOrEmpty() }
        ?.map { it + Chunk.HEADER_SIZE }
        ?: throw Exception("Skill data index [${Chunk.PCSK}] is missing or empty.")

    /**
     * Reads skill data from a given starting address.
     */
    fun Int.addressToSkill(requiredType: Skill? = null): PlayerSkill? {
        val type = save.bytes.leEnum<Skill>(this)
        if (type != (requiredType ?: type) || type == Skill.Nothing)
            return null
        return PlayerSkill(
            type = save.bytes.leEnum<Skill>(this),
            state = save.bytes.leEnum<PlayerSkill.State>(this + 4),
            activeTraining = save.bytes.leNum<Int>(this + 8),
            inactiveTraining = save.bytes.leNum<Int>(this + 12),
            level = save.bytes[this + 16],
            advancement = save.bytes[this + 17],
        )
    }

    /**
     * Reads player skill data for a single skill.
     *
     * @return `null` if the player character does not have the skill.
     */
    fun readSkill(type: Skill): PlayerSkill? =
        addresses.firstNotNullOfOrNull { it.addressToSkill(type) }

    /**
     * Reads player skill data present in the save data.
     */
    fun readSkills(includeMissing: Boolean = false): List<PlayerSkill> =
        addresses.mapNotNull { it.addressToSkill() }.let { acquired ->
            if (includeMissing)
                acquired + Skill.entries
                    .filterNot { target -> acquired.any { target.id == it.id } }
                    .map {
                        PlayerSkill(
                            type = it,
                            state = NotAvailable,
                            level = -1,
                            advancement = -1,
                            inactiveTraining = 0,
                            activeTraining = 0,
                        )
                    }
            else acquired
        }


    /**
     * Write skill data to the save data.
     *
     * Automatically uses the first empty skill slot if [skill] is one that the player doesn't yet have.
     */
    fun writeSkill(skill: PlayerSkill) {
        val address = addresses.first { save.bytes.leEnum<Skill>(it) in setOf(skill.type, Skill.Nothing) }

        save.bytes.leWrite(
            address, listOf(
                skill.id,
                skill.state.id,
                skill.activeTraining,
                skill.inactiveTraining,
                skill.level,
                skill.advancement
            )
        )
    }
}