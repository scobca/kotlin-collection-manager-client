package org.itmo.kotlincollectionmanagerclient

import org.itmo.kotlincollectionmanagerclient.services.InvokerService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinCollectionManagerClientApplication(private val invoker: InvokerService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        invoker.run()
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinCollectionManagerClientApplication>(*args)
}
