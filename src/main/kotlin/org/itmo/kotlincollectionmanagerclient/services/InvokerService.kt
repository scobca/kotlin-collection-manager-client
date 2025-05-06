package org.itmo.kotlincollectionmanagerclient.services

import org.itmo.kotlincollectionmanagerclient.commands.HistoryCommand
import org.itmo.kotlincollectionmanagerclient.storages.CommandsHistory
import org.itmo.kotlincollectionmanagerclient.utils.CommandValidatorDistributor
import org.itmo.kotlincollectionmanagerclient.utils.ServerWatcherUtil
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
            val command = line.trim().split(" ")[0]
            val args = line.split(" ").drop(1)

            if (line == "") {
                print("> ")
                continue
            }

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

            println(response)
            print("> ")
        }
    }
}