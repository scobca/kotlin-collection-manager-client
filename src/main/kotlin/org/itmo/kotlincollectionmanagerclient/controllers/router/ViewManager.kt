package org.itmo.kotlincollectionmanagerclient.controllers.router

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.util.Callback
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class ViewManager(private val context: ApplicationContext) {
    private lateinit var primaryStage: Stage

    fun setPrimaryStage(stage: Stage) {
        this.primaryStage = stage
    }

    fun showPage(fxmlPath: String) {
        val loader = FXMLLoader(javaClass.getResource(fxmlPath))
        loader.controllerFactory = Callback { clazz -> context.getBean(clazz) }

        val root = loader.load<Parent>()
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }
}