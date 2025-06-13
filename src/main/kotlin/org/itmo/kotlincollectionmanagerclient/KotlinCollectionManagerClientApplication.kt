package org.itmo.kotlincollectionmanagerclient

import javafx.application.Application
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KotlinCollectionManagerClientApplication()

fun main(args: Array<String>) {
    Application.launch(KotlinFxApp::class.java, *args)
}
