package models

import content.Skill

data class PlayerSkill(
    val type: Skill,
    val level: Byte,
    val trainingLevel: Byte,
    val maxDelta: Int,
) {
    val id = type.id
    val name = type.name
    val maxLevel = level + maxDelta + trainingLevel
}