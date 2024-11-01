package gui.checksums

import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import utils.toHex

open class ChecksumController {
    @FXML private lateinit var computed: TableColumn<Any, Any>
    @FXML private lateinit var checksumTable: TableView<Any>

    @FXML
    protected fun refresh(event: Event) {
        computed.setCellFactory {
            object : TableCell<Any, Any>() {
                override fun updateItem(item: Any?, empty: Boolean) {
                    val index = indexProperty().value
                    if (index < 0 || index >= Main.Save.checksums.size) {
                        return
                    }
                    val checksum = Main.Save.checksums[index]
                    this.text = checksum.computedHash.toHex()
                    if (checksum.isMismatched())
                        this.styleClass.add("mismatched")
                }
            }
        }

        checksumTable.items.clear()
        Main.Save.computeChecksums()

        val items = Main.Save.checksums.mapIndexed { index, checksum ->
            mapOf(
                "index" to index,
                "segment" to checksum.segment.let { "${it.first.toHex()}..${it.last.toHex()}" },
                "computed" to checksum.computedHash.toHex(),
                "stored" to checksum.storedHash.toHex(),
            )
        }
        checksumTable.items.addAll(items)
    }
}