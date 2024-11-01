import java.io.File

class SaveFile(val file: File) : SaveData(file.readBytes())