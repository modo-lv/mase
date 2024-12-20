package components

import content.Profession
import content.Skill
import content.Skill.*
import models.PlayerSkill

typealias SkillMinimums = Map<Skill, Dice>

/**
 * Skill advancement data for ADOM professions.
 */
object SkillAdvancement {
    private val minimums = mutableMapOf<Profession, SkillMinimums>()

    operator fun get(profession: Profession): SkillMinimums =
        minimums[profession]!!

    operator fun get(profession: Profession, skill: Skill): Dice =
        get(profession)[skill]!!

    /**
     * Determines which set of dice would be used to roll skill advancement on next level-up.
     *
     * Skill advancement dice (AD) are determined by the [Schedule],
     * according to [PlayerSkill.level] (SL; 1-100) and [PlayerSkill.advancement] (AL; 0-15).
     *
     * The starting AD (at SL 1 and AL 0) for all skills and professions is `2d4`
     * (4th position in the schedule).
     * As SL grows, the AD gets smaller - 1 step down the schedule for every 10 points of SL
     * (the starting 1 included), until it reaches the minimum for the [Profession] for this
     * [Skill] (as per [minimums]).
     *
     * AL does the opposite -- each point of AL moves 1 step up the schedule, making the AD bigger,
     * until it reaches the maximum (`4d5`).
     */
    fun computeDice(profession: Profession, skill: PlayerSkill): Dice {
        val minimum = Schedule.indexOf(get(profession, skill.type))
        val actual = Schedule.indexOf(Dice.`2d4`) - (skill.level / 10) + skill.advancement
        return Schedule[actual.coerceIn(minimum .. Schedule.lastIndex)]
    }

    /**
     * The base max value is
     * [PlayerSkill.level] + [PlayerSkill.activeTraining] + [PlayerSkill.inactiveTraining].
     *
     * However, during level-up, active training is temporarily increased by
     * `(max(diceRoll, diceRoll) / 2) + random(0..3)`
     * (according to http://www.it-is-law.com/adom/index.php?title=Skills),
     * so the actual max value is not exactly predictable.
     */
    fun maxLevel(profession: Profession, skill: PlayerSkill): IntRange =
        skill.run {
            val die = computeDice(profession, skill)
            val min = ((level + activeTraining + inactiveTraining) + die.min).coerceIn(level.toInt(), 100)
            val max = ((level + activeTraining + inactiveTraining) + die.max + 3).coerceIn(min, 100)
            min .. max
        }


    init {
        // Most skills for most professions have a minimum "die" of just +1. The rest are listed below.

        /** Fighter **/
        add(Profession.Fighter, mapOf(
            Dice.`1d3` to setOf(
                Alertness, Archery, BridgeBuilding, Courage, DetectTraps, DisarmTraps, FindWeakness,
                Fletchery, FoodPreservation, Haggling, Herbalism, Listening, Metallurgy, Mining,
                Survival, Swimming, Tactics, Woodcraft
            ),
            Dice.`1d5` to setOf(Climbing, Dodge, FirstAid, Healing, Smithing),
            Dice.`2d4` to setOf(Athletics, TwoWeaponCombat)
        ))

        /** Paladin **/
        add(Profession.Paladin, mapOf(
            Dice.`1d3` to setOf(
                Alchemy, Archery, BridgeBuilding, DetectTraps, DisarmTraps, Dodge, FindWeakness,
                FoodPreservation, Haggling, Listening, Literacy, Music, Survival, Swimming, Tactics,
                TwoWeaponCombat
            ),
            Dice.`1d5` to setOf(Alertness, Climbing, Courage, FirstAid, Healing, Law, Metallurgy, Smithing),
            Dice.`2d4` to setOf(Athletics, Concentration)
        ))

        /** Ranger **/
        add(Profession.Ranger, mapOf(
            Dice.`1d3` to setOf(
                Archery, Backstabbing, BridgeBuilding, Concentration,
                Cooking, Gardening, Mining, Music, Smithing, Swimming,
            ),
            Dice.`1d5` to setOf(
                Alertness, Dodge, FindWeakness, FirstAid, Fletchery, Healing, Metallurgy, Stealth
            ),
            Dice.`2d4` to setOf(
                Athletics, Climbing, DetectTraps, FoodPreservation,
                Herbalism, Listening, Survival, TwoWeaponCombat, Woodcraft
            )
        ))

        /** Wizard **/
        add(Profession.Wizard, mapOf(
            Dice.`1d3` to setOf(
                Alertness, Athletics, BridgeBuilding, Climbing, Cooking, DetectTraps, DisarmTraps,
                FindWeakness, FirstAid, Gardening, Haggling, Healing, Music, PickLocks, PickPockets, Stealth,
                Ventriloquism
            ),
            Dice.`1d5` to setOf(FoodPreservation, Gemology, Listening, Metallurgy, Necromancy),
            Dice.`2d4` to setOf(Alchemy, Concentration, Herbalism, Literacy)
        ))

        /** Mindcrafter **/
        add(Profession.Mindcrafter, mapOf(
            Dice.`1d3` to setOf(BridgeBuilding, Swimming, Ventriloquism),
            Dice.`1d5` to setOf(Gemology, Haggling, Listening),
            Dice.`2d4` to setOf(Alertness, Concentration, Literacy)
        ))
    }

    fun add(key: Profession, overrides: Map<Dice, Set<Skill>> = emptyMap()) {
        minimums[key] =
            Skill.entries.associateWith { Dice.`1` } + overrides.flatMap { override ->
                override.value.map { it to override.key }
            }
    }

    val Schedule = listOf(
        Dice.`1`,
        Dice.`1d3`,
        Dice.`1d5`,
        Dice.`2d4`,
        Dice.`3d3`,
        Dice.`3d4`,
        Dice.`3d5`,
        Dice.`4d4`,
        Dice.`4d5`
    )
}