package gui.models

import SaveData
import java.io.File

class SaveFileModel(val file: File) : SaveData<SaveFileModel>(file.readBytes()) {
    override fun computeChecksums(): SaveFileModel {
        // TODO: Trigger checksum observer update
        return super.computeChecksums()
    }
}