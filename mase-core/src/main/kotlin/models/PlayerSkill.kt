package models

import content.Skill

data class PlayerSkill(
    val type: Skill,
    val acquired: Boolean = true,
    val level: Byte,
    val practicalBonus: Int,
    val theoreticalBonus: Byte,
) {
    val id = type.id
    val name = type.name
    val maxLevel = level + practicalBonus + theoreticalBonus
}