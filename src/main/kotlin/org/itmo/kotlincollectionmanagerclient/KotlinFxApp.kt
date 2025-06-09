package org.itmo.kotlincollectionmanagerclient

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component

@Component
class KotlinFxApp : Application() {
    private lateinit var context: ConfigurableApplicationContext

    override fun init() {
        context = SpringApplicationBuilder(KotlinCollectionManagerClientApplication::class.java)
            .headless(false)
            .run()
    }

    override fun start(primaryStage: Stage) {
        val viewManager = context.getBean(ViewManager::class.java)
        viewManager.setPrimaryStage(primaryStage)

        viewManager.showPage("/fxml/LoginPage.fxml")
    }

    override fun stop() {
        context.close()
        Platform.exit()
    }

}