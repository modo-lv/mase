package gui.checksums

import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.TableView
import utils.toHex

open class ChecksumController {
    @FXML private lateinit var table: TableView<Any>

    @FXML
    protected fun refresh(event: Event) {
        Main.Save.computeChecksums()

        val items = Main.Save.checksums.mapIndexed { index, checksum ->
            mapOf(
                "index" to index,
                "segment" to checksum.segment.let { "${it.first.toHex()}..${it.last.toHex()}" },
                "computed" to checksum.computedHash.toHex(),
                "stored" to checksum.storedHash.toHex(),
            )
        }
        table.items.clear()
        table.items.addAll(items)
    }
}