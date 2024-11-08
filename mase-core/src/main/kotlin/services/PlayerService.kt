package services

import SaveData
import models.PlayerCharacter

/**
 * Provides reading player attributes, location, etc. from the save data.
 */
class PlayerService(val save: SaveData<*>) {
    val skills = SkillService(save)
    val character = PlayerCharacter(save.bytes)
}