package gui.tabs

import Main
import com.sun.javafx.collections.ObservableListWrapper
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import structure.ChecksumSegment
import utils.toHex

class ChecksumController {
    @FXML
    private lateinit var checksumTable: TableView<ChecksumSegment>

    @FXML
    private lateinit var segment: TableColumn<ChecksumSegment, String>

    @FXML
    private lateinit var computed: TableColumn<ChecksumSegment, String>

    @FXML
    private lateinit var stored: TableColumn<ChecksumSegment, String>

    // Manually triggered observable property to use on checksum refresh
    private var checksumChangeTrigger = SimpleBooleanProperty()

    @FXML
    private fun refresh(event: Event) {
        if ((event.target as Tab).isSelected) {
            Main.Save.computeChecksums()
            checksumChangeTrigger.set(!checksumChangeTrigger.get())
        }
    }

    @FXML
    private fun initialize() {
        checksumTable.items = ObservableListWrapper(Main.Save.checksumSegments)

        segment.setCellValueFactory {
            // Segments aren't updated during editor operation, so we don't need to bind to a real property
            SimpleStringProperty(it.value.range.toHex())
        }
        computed.setCellValueFactory {
            Bindings.createStringBinding({ it.value.computedChecksum.toHex() }, checksumChangeTrigger)
        }
        stored.setCellValueFactory {
            Bindings.createStringBinding({ it.value.storedChecksum.toHex() }, checksumChangeTrigger)
        }

        computed.setCellFactory {
            object : TableCell<ChecksumSegment, String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    val index = indexProperty().value
                    if (index < 0 || index >= Main.Save.checksumSegments.size) {
                        return
                    }
                    val checksum = Main.Save.checksumSegments[index]
                    this.text = item
                    if (checksum.isMismatched())
                        this.styleClass.add("mismatched")
                    else
                        this.styleClass.remove("mismatched")
                }
            }
        }
    }
}