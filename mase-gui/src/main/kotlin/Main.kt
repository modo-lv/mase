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
        stage.apply {
            scene = FXMLLoader().run {
                location = javaClass.getResource("/gui/main.fxml")
                load<Scene>().apply {
                    stylesheets.add("/gui/style.css")
                }
            }
            minWidth = 640.0
            minHeight = 480.0
            show()
        }
    }

    companion object {
        val SaveProperty = SimpleObjectProperty<SaveFileModel>(null)
        var Save: SaveFileModel? by delegateTo(SaveProperty)
    }
}

fun main(vararg args: String) {
    if (args.isNotEmpty())
        Main.Save = SaveFileModel(file = File(args[0]))
    Application.launch(Main::class.java, *args)
}