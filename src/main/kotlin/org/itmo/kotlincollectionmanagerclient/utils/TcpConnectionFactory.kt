package org.itmo.kotlincollectionmanagerclient.utils

import java.io.PrintWriter
import java.net.ConnectException
import java.net.Socket

object TcpConnectionFactory {
    fun sendMessage(message: String): String {
        try {
            Socket("localhost", 6000).use { socket ->
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