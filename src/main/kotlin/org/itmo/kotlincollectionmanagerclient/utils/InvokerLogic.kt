package org.itmo.kotlincollectionmanagerclient.utils

import org.itmo.kotlincollectionmanagerclient.commands.ExecuteCommand
import org.itmo.kotlincollectionmanagerclient.commands.HistoryCommand
import org.itmo.kotlincollectionmanagerclient.storages.CommandsHistory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class InvokerLogic(
    private val tcpConnectionFactory: TcpConnectionFactory,
    private val serverWatcher: ServerWatcherUtil,
    private val distributor: CommandValidatorDistributor,
    private val historyCommand: HistoryCommand,
    private val commandsHistory: CommandsHistory,
    @Lazy private val executeCommand: ExecuteCommand,
) {
    private val validatableCommands = listOf<String>("insert", "update", "replaceIfLower")

    fun handleCommand(line: String): String {
        val parts = line.trim().split(" ")
        val command = parts.firstOrNull() ?: ""
        val args = parts.drop(1)

        if (line.trim() == "") {
            print("> ")
            return ""
        }

        if (command == "exit") {
            println("Application stopped.")
            return "STOP"
        }

        if (command == "execute") {
            executeCommand.execute(args)
            return ""
        }

        if (validatableCommands.contains(command)) {
            if (serverWatcher.checkConnection()) {
                println(distributor.distribute(command, args))
                print("> ")
                return ""
            } else {
                return ""
            }
        }

        if (command == "history") {
            historyCommand.run()
            return ""
        }

        val response = tcpConnectionFactory.sendMessage(line)

        if (!response.contains("Command $command not found")) {
            commandsHistory.addCommand(command)
        }

        if (command == "help") {
            val correctResponse = StringToMapParser.parseToMap(response)
            val bold = "\u001B[1m"
            val reset = "\u001B[0m"

            if (!correctResponse.isEmpty()) {
                correctResponse.forEach { (key, value) ->
                    println("${bold}$key ${reset}: $value")
                }
            } else {
                println("Commands not loaded")
            }
            print("> ")
            return ""
        }

        println(response)
        print("> ")
        return ""
    }
}