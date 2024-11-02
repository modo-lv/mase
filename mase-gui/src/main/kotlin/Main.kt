import gui.Model
import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.application.Application
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.TabPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File

class Main : Application() {
    @FXML lateinit var mainTabs: TabPane
    private val logger = KotlinLogging.logger { }

    @FXML fun initialize() {
        mainTabs.disableProperty().set(Save == null)
    }

    val fileName get() = Save?.file.toString()

    fun save() {
        logger.info { "Writing [${Save?.file}]..." }
        Save?.fixChecksums()
        Save?.let { it.file.writeBytes(it.bytes) }
    }

    override fun start(stage: Stage) {
        val vbox = FXMLLoader().run {
            location = javaClass.getResource("/gui/main.fxml")
            load<VBox>().apply {
                stylesheets.add("/gui/style.css")
            }
        }

        stage.apply {
            scene = Scene(vbox)
            minHeight = 500.0
            minWidth = 600.0
            show()
        }
    }

    companion object {
        private val _Save = SimpleObjectProperty<SaveFile>(null)
        var Save: SaveFile?
            get() = _Save.get()
            set(value) = _Save.set(value)
        val SaveProperty get() = _Save
    }
}

fun main(vararg args: String) {
    //println(System.getProperty("sun.arch.data.model"))
    if (args.isNotEmpty())
        Main.Save = SaveFile(file = File(args[0])).computeChecksums()
    Model.initialize()
    Application.launch(Main::class.java, *args)
}