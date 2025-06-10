package org.itmo.kotlincollectionmanagerclient

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.itmo.kotlincollectionmanagerclient.controllers.i18n.Localizer
import org.itmo.kotlincollectionmanagerclient.controllers.router.ViewManager
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import java.util.Locale

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
        val localizer = context.getBean(Localizer::class.java)

        viewManager.setPrimaryStage(primaryStage)

        val locale = Locale.Builder().setLanguage(localizer.getLanguage()).build()
        viewManager.showPage("/fxml/LoginPage.fxml", localizer.getBundle(locale))
    }

    override fun stop() {
        context.close()
        Platform.exit()
    }
}