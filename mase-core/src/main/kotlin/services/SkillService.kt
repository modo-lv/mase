package services

import SaveData
import content.Skill
import models.PlayerSkill
import structure.Chunk
import utils.boolean
import utils.leEnum
import utils.leNum
import utils.leWrite
import java.lang.Exception

/**
 * Reads and writes player skill values to/from save data.
 *
 * Each skill is in its own [Chunk.PCSK], in the [Chunk.GSKD]. They are saved ordered by their IDs.
 *
 * Each PCSK segment content is:
 *  [Int] ID
 *  [Int] 0 for empty slots, 1 for skills player has
 *  [Int] Skill max delta. Skill maximum is {skill level + this + training level}.
 *  [Int] Unknown number (maybe training marks?)
 * [Byte] Skill level
 * [Byte] Training level: 1-15.
 *        Affects the maximum level and improvement dice (from [+1] to [+4d5]).
 *        The exact value of the dice for each level depends on the current skill level:
 *        as the skill value goes up, the dice levels go down. Only 0x0F is +4d5 even at 99.
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
        val playerHas = save.bytes.boolean(this + 4)
        if (type != (requiredType ?: type) || type == Skill.Nothing || !playerHas)
            return null
        return PlayerSkill(
            type = save.bytes.leEnum<Skill>(this),
            maxDelta = save.bytes.leNum<Int>(this + 8),
            level = save.bytes[this + 16],
            trainingLevel = save.bytes[this + 17],
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
                            level = -1,
                            trainingLevel = -1,
                            maxDelta = 0
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

        save.bytes.leWrite(address, listOf(skill.id, 1, skill.maxDelta))
        save.bytes[address + 16] = skill.level
        save.bytes[address + 17] = skill.trainingLevel
    }
}