package controllers

import Main
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Tab
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import ktfx.bindings.bindingBy
import ktfx.bindings.stringBindingOf
import ktfx.collections.emptyObservableList
import ktfx.collections.toObservableList
import structure.ChecksumSegment
import utils.toHex

class ChecksumTabController {
    lateinit var checksumTab: Tab
    lateinit var checksumTable: TableView<ChecksumSegment>
    lateinit var num: TableColumn<ChecksumSegment, String>
    lateinit var range: TableColumn<ChecksumSegment, String>
    lateinit var computed: TableColumn<ChecksumSegment, String>
    lateinit var stored: TableColumn<ChecksumSegment, String>

    fun initialize() {
        checksumTable.itemsProperty().bind(
            Main.SaveProperty.bindingBy {
                (it?.checksumSegments?.toObservableList() ?: emptyObservableList<ChecksumSegment>())
            }
        )

        computed.setCellFactory {
            object : TableCell<ChecksumSegment, String>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    if (Main.Save == null || item == null) {
                        this.text = null
                        return
                    }
                    val index = indexProperty().value
                    if (index < 0 || index >= Main.Save!!.checksumSegments.size) {
                        return
                    }
                    val checksum = Main.Save!!.checksumSegments[index]
                    this.text = item
                    if (checksum.isMismatched())
                        this.styleClass.add("mismatched")
                    else
                        this.styleClass.remove("mismatched")
                }
            }
        }

        Main.SaveProperty.addListener { _, _, _ -> handleNewSave() }
        handleNewSave()
    }

    /**
     * Handles switching between tabs.
     */
    fun handleSelectionChange() {
        if (checksumTab.isSelected)
            Main.Save!!.computeChecksums()
    }

    /**
     * Handles opening another save file.
     */
    fun handleNewSave() {
        if (Main.Save == null) {
            return
        }
        Main.Save!!.computeChecksums()

        // Segments only update when switching save files, so non-checksum properties
        // don't need to be active bindings
        num.setCellValueFactory {
            it.tableColumn
            SimpleStringProperty(Main.Save!!.checksumSegments.indexOf(it.value).toString())
        }
        range.setCellValueFactory { SimpleStringProperty(it.value.range.toHex()) }

        computed.setCellValueFactory {
            stringBindingOf(Main.Save!!.checksumUpdateTrigger) { it.value.computedChecksum.toHex() }
        }
        stored.setCellValueFactory {
            stringBindingOf(Main.Save!!.checksumUpdateTrigger) { it.value.storedChecksum.toHex() }
        }
    }
}