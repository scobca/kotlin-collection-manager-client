package org.itmo.kotlincollectionmanagerclient.services

import org.itmo.kotlincollectionmanagerclient.commands.HistoryCommand
import org.itmo.kotlincollectionmanagerclient.storages.CommandsHistory
import org.itmo.kotlincollectionmanagerclient.utils.CommandValidatorDistributor
import org.itmo.kotlincollectionmanagerclient.utils.ServerWatcherUtil
import org.itmo.kotlincollectionmanagerclient.utils.StringToMapParser
import org.itmo.kotlincollectionmanagerclient.utils.TcpConnectionFactory
import org.springframework.stereotype.Service
import java.util.Scanner

@Service
class InvokerService(
    private val distributor: CommandValidatorDistributor,
    private val serverWatcher: ServerWatcherUtil,
    private val historyCommand: HistoryCommand,
    private val commandsHistory: CommandsHistory,
) {
    private var runtime = true
    private val validatableCommands = listOf<String>("insert", "update", "replaceIfLower")

    fun run() {
        val scanner = Scanner(System.`in`)

        print("> ")
        while (runtime) {
            val line = scanner.nextLine().trim()
            val parts = line.trim().split(" ")
            val command = parts.firstOrNull() ?: ""
            val args = parts.drop(1)

            if (line == "") {
                print("> ")
                continue
            }

            if (command == "exit") println("Application stopped."); runtime = false; continue

            if (validatableCommands.contains(command)) {
                if (serverWatcher.checkConnection()) {
                    println(distributor.distribute(command, args))
                    print("> ")
                    continue
                } else {
                    continue
                }
            }

            if (command == "history") {
                historyCommand.run()
                continue
            }

            val response = TcpConnectionFactory.sendMessage(line)

            if (!response.contains("Command $command not found")) {
                commandsHistory.addCommand(command)
            }

            if (command == "help") {
                val correctResponse = StringToMapParser.parseToMap(response)
                val bold = "\u001B[1m"
                val reset = "\u001B[0m"

                correctResponse.forEach { (key, value) ->
                    println("${bold}$key ${reset}: $value")
                }
                print("> ")
                continue
            }

            println(response)
            print("> ")
        }
    }
}