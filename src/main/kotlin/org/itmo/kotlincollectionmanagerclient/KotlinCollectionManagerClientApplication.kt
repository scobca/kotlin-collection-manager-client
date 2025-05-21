package org.itmo.kotlincollectionmanagerclient

import org.itmo.kotlincollectionmanagerclient.services.AuthInvoker
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinCollectionManagerClientApplication(private val authInvoker: AuthInvoker) : CommandLineRunner {
    override fun run(vararg args: String?) {
        authInvoker.run()
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinCollectionManagerClientApplication>(*args)
}
