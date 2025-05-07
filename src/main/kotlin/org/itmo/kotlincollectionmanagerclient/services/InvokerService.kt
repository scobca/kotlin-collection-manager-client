package org.itmo.kotlincollectionmanagerclient.services

import org.itmo.kotlincollectionmanagerclient.utils.InvokerLogic
import org.springframework.stereotype.Service
import java.util.Scanner

@Service
class InvokerService(
    private val invokerLogic: InvokerLogic,
) {
    private var runtime = true

    fun run() {
        val scanner = Scanner(System.`in`)

        print("> ")
        while (runtime) {
            val line = scanner.nextLine().trim()
            val response = invokerLogic.handleCommand(line)

            if (response == "STOP") runtime = false
            if (response == "") continue
        }
    }

    fun stop() = !runtime
}