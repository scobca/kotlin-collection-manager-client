package org.itmo.kotlincollectionmanagerclient.utils

import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

object TcpConnectionFactory {
    fun connectToServer() {
        thread {
            val socket = Socket("localhost", 6000)
            val output = PrintWriter(socket.getOutputStream(), true)
            val input = socket.getInputStream().bufferedReader()

            output.println("Hello from client")

            // Читаем ответ от сервера
            val response = input.readLine()
            println("Received from server: $response")

            socket.close()
        }
    }
}