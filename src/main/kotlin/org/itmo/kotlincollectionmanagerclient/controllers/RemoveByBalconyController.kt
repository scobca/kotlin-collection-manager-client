package org.itmo.kotlincollectionmanagerclient.controllers

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.text.Text
import javafx.stage.Stage
import org.itmo.kotlincollectionmanagerclient.controllers.i18n.Localizer
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.itmo.kotlincollectionmanagerclient.services.CommandsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Locale
import java.util.ResourceBundle

@Component
class RemoveByBalconyController {
    @Autowired
    private lateinit var localizer: Localizer

    @Autowired
    private lateinit var router: ViewManager

    @Autowired
    private lateinit var commandsService: CommandsService

    @FXML lateinit var balconyType: ChoiceBox<Boolean>

    @FXML lateinit var closeButton: Button
    @FXML lateinit var okButton: Button

    @FXML lateinit var popupHintLabel: Text
    @FXML lateinit var popupLabel: Label

    private lateinit var currentBundle: ResourceBundle
    private lateinit var stage: Stage

    @FXML
    fun initialize() {
        val lang = localizer.getLanguage()
        updateBundle(Locale.forLanguageTag(lang))

        balconyType.getItems().addAll(true, false)
    }

    private fun updateBundle(locale: Locale) {
        ResourceBundle.clearCache()
        currentBundle = ResourceBundle.getBundle("i18n.Bundle", locale)
        updateTexts()
    }

    fun updateTexts() {
        closeButton.text = currentBundle.getString("removeByBalcony.close")
        okButton.text = currentBundle.getString("removeByBalcony.ok")
        popupLabel.text = currentBundle.getString("removeByBalcony.title")
        popupHintLabel.text = currentBundle.getString("removeByBalcony.idFieldHint")
    }

    @FXML
    fun remove() {
        if (balconyType.value == null) {
            balconyType.style = "-fx-border-color: red;"

            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Ошибка в данных"
            alert.headerText = "Повторите ввод данных в корректном формате!"
            alert.showAndWait()
        } else {
            commandsService.removeByBalcony(balconyType.value)

            close()
            router.showPage("/fxml/MainPage.fxml", currentBundle)
        }
    }

    @FXML
    fun close() = stage.close()

    fun setStage(stage: Stage) {
        this.stage = stage
    }
}