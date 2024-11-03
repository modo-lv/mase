package gui.models

import SaveData
import java.io.File

class SaveFileModel(val file: File) {
    val _underlying = SaveData(file.readBytes())



    fun computeChecksums() {
        _underlying.computeChecksums()
        // TODO: Trigger checksum observer update
    }
}