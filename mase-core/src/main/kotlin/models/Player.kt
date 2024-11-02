package models

/**
 * Combines more specialized player property classes into a single entry point.
 */
class Player(val bytes: ByteArray) {
    val character = PlayerCharacter(bytes)
}