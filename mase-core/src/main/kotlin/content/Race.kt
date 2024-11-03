package content

enum class Race(id: Int) {
    Human(0x00),
    Troll(0x01),
    HighElf(0x02),
    GrayElf(0x03),
    DarkElf(0x04),
    Dwarf(0x05),
    Gnome(0x06),
    Hurthling(0x07),
    Orc(0x08),
    Drakeling(0x09),
    MistElf(0x0A),
    Ratling(0x0B),

    ;

    companion object {
        const val ADDRESS = 0x1D
    }
}