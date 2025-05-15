package org.itmo.kotlincollectionmanagerclient.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.PrintWriter
import java.net.ConnectException
import java.net.Socket

@Component
class TcpConnectionFactory {
    @Value("\${config.tcp-server-host}")
    private lateinit var tcpServerHost: String

    @Value("\${config.tcp-server-port}")
    private lateinit var tcpServerPort: String

    fun sendMessage(message: String): String {
        try {
            Socket(tcpServerHost, tcpServerPort.toInt()).use { socket ->
                val output = PrintWriter(socket.getOutputStream(), true)
                val input = socket.getInputStream().bufferedReader()

                output.println(message)
                return input.readLine() ?: "No response from server"
            }
        } catch (_: ConnectException) {
            return "(!) Error sending message: Server not available. Try again later."
        } catch (e: Exception) {
            return "(!) Error sending message: ${e.message}"
        }
    }
}