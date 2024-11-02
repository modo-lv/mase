import java.io.File

fun main(vararg args: String) {
    val save = SaveData(bytes = File(args[0]).readBytes())
    save.sections
}