package utils

@ExperimentalStdlibApi fun Int.toHex() = "0x${this.toHexString(HexFormat.UpperCase)}"
@ExperimentalStdlibApi fun UInt.toHex() = "0x${this.toHexString(HexFormat.UpperCase)}"
@ExperimentalStdlibApi fun IntRange.toHex() = "${this.first.toHex()}..${this.last.toHex()}"