package gui.models

import SaveData
import javafx.beans.property.SimpleBooleanProperty
import java.io.File

class SaveFileModel(val file: File) : SaveData<SaveFileModel>(file.readBytes()) {
    val checksumUpdateTrigger = SimpleBooleanProperty()

    override fun computeChecksums(): SaveFileModel =
        super.computeChecksums().also {
            checksumUpdateTrigger.set(!checksumUpdateTrigger.get())
        }
}