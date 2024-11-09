package models.values

interface ValueModel {
    val name: String

    fun commit()
}