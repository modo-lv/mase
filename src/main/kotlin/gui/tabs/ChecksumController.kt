package gui.tabs

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import structure.Checksum
import utils.toHex

open class ChecksumController {
    @FXML private lateinit var checksumTable: TableView<Checksum>
    @FXML private lateinit var segment: TableColumn<Checksum, String>
    @FXML private lateinit var computed: TableColumn<Checksum, String>
    @FXML private lateinit var stored: TableColumn<Checksum, String>

    @FXML fun initialize() {
        // Bind checksum list to the table
        checksumTable.items = Model.checksumsProperty

        segment.setCellValueFactory {
            // Segments aren't updated during editor operation, so we don't need to bind to a real property
            SimpleStringProperty("${it.value.segment.first.toHex()}..${it.value.segment.last.toHex()}")
        }
        stored.setCellValueFactory {
            Bindings.createStringBinding(
                { it.value.storedHash.toHex() },
                it.value.storedHashProperty
            )
        }
        computed.setCellValueFactory {
            Bindings.createStringBinding(
                { it.value.computedHash.toHex() },
                it.value.computedHashProperty
            )
        }

        computed.setCellFactory {
            object : TableCell<Checksum, String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    val index = indexProperty().value
                    if (index < 0 || index >= Model.checksums.size) {
                        return
                    }
                    val checksum = Model.checksums[index]
                    this.text = item
                    if (checksum.isMismatched())
                        this.styleClass.add("mismatched")
                    else
                        this.styleClass.remove("mismatched")
                }
            }
        }

    }

    @FXML protected fun refresh(event: Event) {
        if ((event.target as Tab).isSelected)
            Main.Save.computeChecksums()
    }
}