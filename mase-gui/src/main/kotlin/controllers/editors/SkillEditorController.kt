package controllers.editors

import components.NumericSpinnerValueFactory
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.control.RadioButton
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.ToggleGroup
import javafx.scene.text.Text
import ktfx.bindings.booleanBindingBy
import models.PlayerSkill
import models.PlayerSkill.State.Available
import models.PlayerSkill.State.NotAvailable
import models.factories.SkillValueFactory
import models.values.SkillValue

class SkillEditorController(val model: SkillValue) {
    lateinit var state: ToggleGroup
    lateinit var stateAvailable: RadioButton
    lateinit var stateNotAvailable: RadioButton

    lateinit var spinners: Node
    lateinit var level: Spinner<Byte>
    lateinit var inactive: Spinner<Int>
    lateinit var advancement: Spinner<Byte>
    lateinit var active: Spinner<Int>

    lateinit var computed: Node
    lateinit var dice: Text
    lateinit var max: Text

    fun initialize() {
        model.stateProperty.addListener { _, _, new -> skillState(new) }

        state.selectedToggleProperty().addListener { _, _, new ->
            if (new == stateAvailable) {
                model.state = Available
                model.level = model.level.coerceAtLeast(1)
                model.advancementLevel = model.advancementLevel.coerceAtLeast(0)
            } else
                model.state = NotAvailable
        }

        stateAvailable.setOnAction { (it.target as RadioButton).isSelected = true }
        stateNotAvailable.setOnAction { (it.target as RadioButton).isSelected = true }

        model.stateProperty.booleanBindingBy { it != Available }.also { toggle ->
            spinners.disableProperty().bind(toggle)
            computed.disableProperty().bind(toggle)
        }

        level.apply {
            @Suppress("UNCHECKED_CAST")
            valueFactory = NumericSpinnerValueFactory(1, 100) as SpinnerValueFactory<Byte>
            valueFactory.valueProperty().bindBidirectional(model.levelProperty)
            if (model.state == Available)
                Platform.runLater { requestFocus(); editor.selectAll() }
        }
        inactive.apply {
            valueFactory = NumericSpinnerValueFactory.createFor(model.inactiveTraining)
            valueFactory.valueProperty().bindBidirectional(model.inactiveTrainingProperty)
        }
        advancement.apply {
            valueFactory = NumericSpinnerValueFactory.createFor(model.advancementLevel)
            valueFactory.valueProperty().bindBidirectional(model.advancementLevelProperty)
        }
        active.apply {
            valueFactory = NumericSpinnerValueFactory.createFor(model.activeTraining)
            valueFactory.valueProperty().bindBidirectional(model.activeTrainingProperty)
        }

        dice.textProperty().bind(SkillValueFactory.advancementDiceBinding(model))
        max.textProperty().bind(SkillValueFactory.advancementMaxBinding(model))

        skillState(model.state)
    }

    private fun skillState(new: PlayerSkill.State) {
        if (new == Available)
            state.selectToggle(stateAvailable)
        else
            state.selectToggle(stateNotAvailable)
    }

}