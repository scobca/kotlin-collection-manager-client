package org.itmo.kotlincollectionmanagerclient

import org.itmo.kotlincollectionmanagerclient.utils.TcpConnectionFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinCollectionManagerClientApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        TcpConnectionFactory.connectToServer()
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinCollectionManagerClientApplication>(*args)
}
