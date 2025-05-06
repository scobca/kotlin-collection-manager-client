package org.itmo.kotlincollectionmanagerclient.utils

import org.springframework.stereotype.Component

@Component
class ServerWatcherUtil {
    fun checkConnection(): Boolean {
        val response = TcpConnectionFactory.sendMessage("Check server connection")

        if (response.contains("(!) Error sending message: Server not available. Try again later.")) {
            println("(!) Error sending message: Server not available. Try again later.")
            print("> ")
            return false
        } else {
            return true
        }
    }
}