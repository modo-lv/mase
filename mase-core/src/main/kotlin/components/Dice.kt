package components

import kotlin.math.max
import kotlin.random.Random
import kotlin.random.nextInt

@Suppress("EnumEntryName")
enum class Dice(val count: Int = 1, val sides: Int) {
    `0`(0, 0),
    `1`(1, 1),
    `1d3`(1, 3),
    `1d5`(1, 5),
    `2d4`(2, 4),
    `3d3`(3, 3),
    `3d4`(3, 4),
    `3d5`(3, 5),
    `4d4`(4, 4),
    `4d5`(4, 5),

    ;

    val min = count
    val max = count * sides

    fun roll(): Int =
        if (count == 0) 0 else {
            (1..count).reduce { acc, _ -> acc + Random.nextInt(1..sides) }
        }

    fun rollWithAdvantage(): Int =
        max(roll(), roll())
}