import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage
import structure.SaveFile
import java.io.File

class Main : Application() {
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
        lateinit var Save: SaveFile
    }
}

fun main(vararg args: String) {
    //println(System.getProperty("sun.arch.data.model"))
    Main.Save = SaveFile(file = File(args[0])).apply { read() }
    Application.launch(Main::class.java, *args)
}