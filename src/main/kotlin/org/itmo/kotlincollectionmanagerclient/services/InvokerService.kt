package org.itmo.kotlincollectionmanagerclient.services

import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage
import org.itmo.kotlincollectionmanagerclient.utils.InvokerLogic
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.util.Scanner

@Service
class InvokerService(
    private val invokerLogic: InvokerLogic,
    @Lazy private val authInvoker: AuthInvoker,
) {
    private var runtime = true

    fun run() {
        val scanner = Scanner(System.`in`)

        print("> ")
        while (runtime) {
            val line = scanner.nextLine().trim()

            val command = line.split(" ")[0]
            if (command == "logout") {
                TokensStorage.setAccessToken(null)
                TokensStorage.setRefreshToken(null)
                authInvoker.run()
                stop()
                continue
            }

            val response = invokerLogic.handleCommand(line)
            
            if (response == "STOP") runtime = false
            if (response == "") continue
        }
    }

    fun stop() = !runtime
}