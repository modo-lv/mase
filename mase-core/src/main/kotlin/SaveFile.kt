import java.io.File

class SaveFile(val file: File) : SaveData<SaveFile>(bytes = file.readBytes())