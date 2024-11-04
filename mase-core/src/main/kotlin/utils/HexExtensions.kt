package utils

fun Int.toHex() = "0x${this.toHexString(HexFormat.UpperCase)}"
fun IntRange.toHex() = "${this.first.toHex()}..${this.last.toHex()}"