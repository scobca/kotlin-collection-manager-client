package org.itmo.kotlincollectionmanagerclient.commands

import org.itmo.kotlincollectionmanagerclient.services.InvokerService
import org.itmo.kotlincollectionmanagerclient.utils.InvokerLogic
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.io.File

@Component
class ExecuteCommand(
    @Lazy private val invokerLogic: InvokerLogic,
    private val invokerService: InvokerService
) {
    fun execute(args: List<String>): String {
        val fileName = args[0].trim()
        val resource = File(fileName)
        var lifetime = true



        if (resource.exists()) {
            if (resource.length() > 0) {
                resource.forEachLine { commandLine ->
                    run {
                        if (lifetime) {
                            val line = commandLine.trim()
                            val response = invokerLogic.handleCommand(line)

                            if (response == "STOP") invokerService.stop()
                            if (response == "") return@run
                        }
                    }
                }
            } else {
                println("Error occurred while executing script. File is empty.")
                print("> ")
                return ""
            }
        } else {
            println("Error occurred. File $fileName doesn't exist.")
            print("> ")
            return ""
        }
        print("> ")
        return ""
    }
}