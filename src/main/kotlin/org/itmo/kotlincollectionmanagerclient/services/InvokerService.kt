package org.itmo.kotlincollectionmanagerclient.services

import org.itmo.kotlincollectionmanagerclient.utils.TcpConnectionFactory
import org.springframework.stereotype.Service
import java.util.Scanner

@Service
class InvokerService {
    private var runtime = true

    fun run() {
        val scanner = Scanner(System.`in`)

        print("> ")
        while (runtime) {
            val line = scanner.nextLine().trim()

            if (line == "") {
                print("> ")
                continue
            }

            val response = TcpConnectionFactory.sendMessage(line)

            println(response)
            print("> ")
        }
    }
}