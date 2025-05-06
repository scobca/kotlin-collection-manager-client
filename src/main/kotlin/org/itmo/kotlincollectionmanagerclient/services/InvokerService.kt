package org.itmo.kotlincollectionmanagerclient.services

import org.itmo.kotlincollectionmanagerclient.storages.CommandsHistory
import org.itmo.kotlincollectionmanagerclient.utils.CommandValidatorDistributor
import org.itmo.kotlincollectionmanagerclient.utils.TcpConnectionFactory
import org.springframework.stereotype.Service
import java.util.Scanner

@Service
class InvokerService(
    private val commandsHistory: CommandsHistory,
    private val distributor: CommandValidatorDistributor
) {
    private var runtime = true
    private val validatableCommands = listOf<String>("insert", "update", "replaceIfLower")

    fun run() {
        val scanner = Scanner(System.`in`)

        print("> ")
        while (runtime) {
            val line = scanner.nextLine().trim()
            val command = line.trim().split(" ")[0]
            val args = line.split(" ").drop(1)

            if (line == "") {
                print("> ")
                continue
            }

            if (validatableCommands.contains(command)) {
                if (checkConnection()) {
                    val response = distributor.distribute(command, args)


                    if (!response.toString().contains("Flat(")) {
                        println(response)
                        print("> ")
                        continue
                    }
                    println(response)
                } else {
                    continue
                }
            }

            if (command == "history") {
                historyCommand()
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

    private fun historyCommand() {
        if (checkConnection()) {
            val response = commandsHistory.getCommandsHistory()
            println(response)

            commandsHistory.addCommand("history")

            print("> ")
        }
    }

    private fun checkConnection(): Boolean {
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