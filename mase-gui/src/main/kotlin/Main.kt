import gui.models.SaveFileModel
import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.application.Application
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.File

class Main : Application() {
    private val logger = KotlinLogging.logger { }

    override fun start(stage: Stage) {
        stage.apply {
            scene = FXMLLoader().run {
                location = javaClass.getResource("/gui/main.fxml")
                load<Scene>().apply {
                    stylesheets.add("/gui/style.css")
                }
            }
            minHeight = 500.0
            minWidth = 600.0
            show()
        }
    }

    companion object {
        private val _save = SimpleObjectProperty<SaveFileModel>(null)
        var Save: SaveFileModel?
            get() = _save.get()
            set(value) = _save.set(value)
        val SaveProperty get() = _save
    }
}

fun main(vararg args: String) {
    //println(System.getProperty("sun.arch.data.model"))
    if (args.isNotEmpty())
        Main.Save = SaveFileModel(file = File(args[0]))
    Application.launch(Main::class.java, *args)
}