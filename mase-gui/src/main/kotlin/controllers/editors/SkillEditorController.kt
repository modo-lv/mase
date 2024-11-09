package controllers.editors

import components.NumericSpinnerValueFactory
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.CheckBox
import javafx.scene.control.RadioButton
import javafx.scene.control.Spinner
import javafx.scene.control.ToggleGroup
import models.PlayerSkill
import models.PlayerSkill.State.Available
import models.PlayerSkill.State.NotAvailable
import models.values.SkillValue

class SkillEditorController(val model: SkillValue) {
    lateinit var state: ToggleGroup
    lateinit var stateAvailable: RadioButton
    lateinit var stateNotAvailable: RadioButton

    lateinit var level: Spinner<Byte>
    lateinit var practical: Spinner<Int>
    lateinit var theoretical: Spinner<Byte>
    lateinit var marks: Spinner<Int>

    fun initialize() {
        model.stateProperty.addListener { _, _, new -> skillState(new) }

        state.selectedToggleProperty().addListener { _, _, new ->
            if (new == stateAvailable)
                model.state = Available
            else
                model.state = NotAvailable
        }

        level.apply {
            valueFactory = NumericSpinnerValueFactory.createFor(model.level)
            valueFactory.valueProperty().bindBidirectional(model.levelProperty)
            if (model.state == Available)
                Platform.runLater { requestFocus(); editor.selectAll() }
        }
        practical.apply {
            valueFactory = NumericSpinnerValueFactory.createFor(model.inactiveTraining)
            valueFactory.valueProperty().bindBidirectional(model.inactiveTrainingProperty)
        }
        theoretical.apply {
            valueFactory = NumericSpinnerValueFactory.createFor(model.advancementLevel)
            valueFactory.valueProperty().bindBidirectional(model.advancementLevelProperty)
        }
        marks.apply {
            valueFactory = NumericSpinnerValueFactory.createFor(model.activeTraining)
            valueFactory.valueProperty().bindBidirectional(model.activeTrainingProperty)
        }

        skillState(model.state)
    }

    private fun skillState(new: PlayerSkill.State) {
        if (new == Available)
            state.selectToggle(stateAvailable)
        else
            state.selectToggle(stateNotAvailable)
    }

}