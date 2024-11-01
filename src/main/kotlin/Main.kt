import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class MainApp : Application() {
    private fun createContent(stage: Stage): Parent =
        VBox(
            Button("Open").apply {
                setOnMouseClicked {
                    FileChooser().run {
                        initialDirectory = File("c:\\users\\martin\\Documents\\ADOM\\adom_steam\\savedg")
                        showOpenDialog(stage)
                    }?.also {
                        Alert(Alert.AlertType.INFORMATION, it.absolutePath).apply {
                            headerText = "File path"
                            showAndWait()
                        }
                    }
                }
            },
            Text("Checksum: ???"),
            Text("File: ${parameters.unnamed[0]}")
        )

    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(stage), 600.0, 300.0)
        stage.show()
    }
}

fun main(vararg args: String) {
    //println(System.getProperty("sun.arch.data.model"))
    //Application.launch(MainApp::class.java, *args)
    val file = File(args[0])
    Save(file = file).also {
        it.read()
    }
}