import javafx.application.Application
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import models.SaveFileModel
import utils.ObservableDelegates.delegateTo
import java.io.File

class Main : Application() {
    override fun start(stage: Stage) {
        Stage = stage.apply {
            scene = FXMLLoader().run {
                location = javaClass.getResource("/gui/main.fxml")
                load<Scene>().apply {
                    stylesheets.add("/gui/style.css")
                }
            }
            minWidth = 640.0
            width = minWidth
            minHeight = 480.0
            height = minHeight
            show()
        }
        if (parameters.raw.isNotEmpty()) {
            Save = SaveFileModel(file = File(parameters.raw[0]))
        }
    }

    companion object {
        val SaveProperty = SimpleObjectProperty<SaveFileModel>(null)
        var Save: SaveFileModel? by delegateTo(SaveProperty)
        lateinit var Stage: Stage
    }
}

fun main(vararg args: String) {
    Application.launch(Main::class.java, *args)
}