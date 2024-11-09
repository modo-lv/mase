package controllers.editors

import models.values.GameValue

interface EditorController {
    val model: GameValue<Number>
}