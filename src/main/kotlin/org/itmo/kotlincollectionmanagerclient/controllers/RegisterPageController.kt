package org.itmo.kotlincollectionmanagerclient.controllers

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import org.itmo.kotlincollectionmanagerclient.controllers.i18n.Localizer
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.itmo.kotlincollectionmanagerclient.exceptions.BasicException
import org.itmo.kotlincollectionmanagerclient.services.AuthService
import org.springframework.stereotype.Component
import java.util.Locale
import java.util.ResourceBundle

@Component
class RegisterPageController(
    private val router: ViewManager,
    private val authService: AuthService,
    private val localizer: Localizer
) {
    @FXML private lateinit var titledPane: TitledPane
    @FXML private lateinit var labelLogin: Label
    @FXML private lateinit var labelPassword: Label
    @FXML private lateinit var registerButton: Button
    @FXML private lateinit var loginButton: Button

    @FXML private lateinit var loginField: TextField
    @FXML private lateinit var passwordField: TextField
    @FXML private lateinit var languageField: ChoiceBox<String>

    private lateinit var currentBundle: ResourceBundle

    @FXML
    fun initialize() {
        languageField.getItems().addAll("ru", "no", "es", "el")
        languageField.value = localizer.getLanguage()

        updateBundle(Locale.forLanguageTag(languageField.value))

        languageField.setOnAction { event ->
            val selected: String? = languageField.value

            localizer.setLanguage(selected.toString())
            localizer.updateLocalization("/fxml/RegisterPage.fxml")
        }
    }

    private fun updateBundle(locale: Locale) {
        ResourceBundle.clearCache()
        currentBundle = ResourceBundle.getBundle("i18n.Bundle", locale)
        updateTexts()
    }

    fun updateTexts() {
        titledPane.text = currentBundle.getString("reg.title.authorization")
        labelLogin.text = currentBundle.getString("reg.label.login")
        labelPassword.text = currentBundle.getString("reg.label.password")
        loginButton.text = currentBundle.getString("reg.button.login")
        registerButton.text = currentBundle.getString("reg.button.register")
    }

    @FXML
    private fun register() {
        try {
            if (authService.register(loginField.text, passwordField.text)) {
                router.showPage("/fxml/MainPage.fxml", currentBundle)
            }
        } catch (e: BasicException) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.headerText = e.message
            alert.showAndWait()
        }
    }

    @FXML
    private fun goToLogin() {
        router.showPage("/fxml/LoginPage.fxml", currentBundle)
    }
}