package org.itmo.kotlincollectionmanagerclient.controllers

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.itmo.kotlincollectionmanagerclient.exceptions.BasicException
import org.itmo.kotlincollectionmanagerclient.services.AuthService
import org.springframework.stereotype.Component

@Component
class RegisterPageController(
    private val router: ViewManager,
    private val authService: AuthService
) {
    @FXML
    private lateinit var registerButton: Button

    @FXML
    private lateinit var loginButton: Button

    @FXML
    private lateinit var loginField: TextField

    @FXML
    private lateinit var passwordField: TextField

    @FXML
    private fun register() {
        try {
            if (authService.register(loginField.text, passwordField.text)) {
                router.showPage("/fxml/MainPage.fxml")
            }
        } catch (e: BasicException) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.headerText = e.message
            alert.showAndWait()
        }
    }

    @FXML
    private fun goToLogin() {
        router.showPage("/fxml/LoginPage.fxml")
    }
}