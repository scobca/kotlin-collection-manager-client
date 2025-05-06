package org.itmo.kotlincollectionmanagerclient.commands

import org.itmo.kotlincollectionmanagerclient.storages.CommandsHistory
import org.itmo.kotlincollectionmanagerclient.utils.ServerWatcherUtil
import org.springframework.stereotype.Component

@Component
class HistoryCommand(
    private val serverWatcher: ServerWatcherUtil,
    private val commandsHistory: CommandsHistory,
) {
    fun run() {
        if (serverWatcher.checkConnection()) {
            val response = commandsHistory.getCommandsHistory()
            println(response)

            commandsHistory.addCommand("history")

            print("> ")
        }
    }
}