package org.itmo.kotlincollectionmanagerclient.services

import org.itmo.kotlincollectionmanagerclient.storages.CommandsHistory
import org.itmo.kotlincollectionmanagerclient.utils.TcpConnectionFactory
import org.springframework.stereotype.Service
import java.util.Scanner

@Service
class InvokerService(private val commandsHistory: CommandsHistory) {
    private var runtime = true

    fun run() {
        val scanner = Scanner(System.`in`)

        print("> ")
        while (runtime) {
            val line = scanner.nextLine().trim()
            val command = line.trim().split(" ")[0]

            if (line == "") {
                print("> ")
                continue
            }

            if (command == "history") {
                val response = commandsHistory.getCommandsHistory()
                commandsHistory.addCommand(command)

                println(response)
                print("> ")
                continue
            }

            val response = TcpConnectionFactory.sendMessage(line)

            if (!response.contains("Command $command not found")) {
                commandsHistory.addCommand(command)
            }

            println(response)
            print("> ")
        }
    }
}