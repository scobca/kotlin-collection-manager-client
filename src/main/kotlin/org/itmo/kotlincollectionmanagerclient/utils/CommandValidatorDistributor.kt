package org.itmo.kotlincollectionmanagerclient.utils

import org.itmo.kotlincollectionmanagerclient.validators.InsertValidator
import org.itmo.kotlincollectionmanagerclient.validators.UpdateValidator
import org.itmo.kotlincollectionmanagerclient.validators.interfaces.BasicCommandValidator
import org.springframework.stereotype.Component

@Component
class CommandValidatorDistributor(
    private val insertValidator: InsertValidator,
    private val updateValidator: UpdateValidator,
) {
    fun distribute(commandName: String, args: List<String>): Any = when (commandName) {
        "insert" -> handleCommand("insert", args, insertValidator)
        "update" -> handleCommand("update", args, updateValidator)
        "replaceIfLower" -> handleCommand("replaceIfLower", args, insertValidator)
        else -> "Wrong command name"
    }

    private fun handleCommand(
        command: String,
        args: List<String>,
        validator: BasicCommandValidator
    ): Any {
        val response = validator.validate(args).toString()
        return if (!response.contains("[") && !response.contains("]")) {
            response
        } else {
            TcpConnectionFactory.sendMessage("$command $response")
        }
    }
}