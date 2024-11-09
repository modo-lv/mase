package controllers.editors

import models.GameValue

interface EditorController {
    val model: GameValue<Number>
}