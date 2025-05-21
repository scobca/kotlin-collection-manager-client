package org.itmo.kotlincollectionmanagerclient.utils

import org.springframework.stereotype.Component

@Component
class ServerWatcherUtil(private val tcpConnectionFactory: TcpConnectionFactory) {
    fun checkConnection(): Boolean {
        val response = tcpConnectionFactory.sendMessage("Check server connection")

        if (response.contains("(!) Error sending message: Server not available. Try again later.")) {
            println("(!) Error sending message: Server not available. Try again later.")
            return false
        } else {
            return true
        }
    }
}