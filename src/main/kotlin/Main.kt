import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.layout.VBox
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
            }
        )

    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(stage), 300.0, 300.0)
        stage.show()
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}