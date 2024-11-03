package content

enum class Gender(override val id: Int) : WithIntId {
    Male(0x00),
    Female(0x01),

    ;

    companion object {
        const val ADDRESS = 0x25
    }
}