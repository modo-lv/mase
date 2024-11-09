package content

enum class SecondaryAttribute(val baseAddress: Int) {
    CarryingCapacity(0x00489F3E),

    /**
     * Signed integer, but the final value (base + modifiers) in game has a minimum of 10.
     *
     * Setting it to anything beyond 1000-2000 is generally useless, though.
     * ADOM splits each of its turns into "segments" (sub-turns),
     * and there seem to be only about 10 in each turn, and a creature can only act once per segment.
     * At least 1000 energy is required to act, and most actions cost <=1000 energy.
     * "Speed" is actually "energy recovery per segment", and 2000 already recovers double the requirement.
     */
    Speed(0x0048A265),
}