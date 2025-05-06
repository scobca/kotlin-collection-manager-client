package org.itmo.kotlincollectionmanagerclient.utils

import org.itmo.kotlincollectionmanagerclient.validators.InsertValidator
import org.itmo.kotlincollectionmanagerclient.validators.UpdateValidator
import org.springframework.stereotype.Component

@Component
class CommandValidatorDistributor(
    private val insertValidator: InsertValidator,
    private val updateValidator: UpdateValidator,
) {
    fun distribute(commandName: String, args: List<String>): Any {
        if (commandName == "insert") {
            val response = insertValidator.validate(args).toString()

            return if (!response.contains("[") && !response.contains("]")) {
                response
            } else {
                println(response + "response")
                TcpConnectionFactory.sendMessage("insert $response")
            }
        }
        if (commandName == "update") {
            val response = updateValidator.validate(args).toString()

            return if (!response.contains("[") && !response.contains("]")) {
                response
            } else {
                TcpConnectionFactory.sendMessage("update $response")
            }
        }
        if (commandName == "replaceIfLower") {
            val response = insertValidator.validate(args).toString()

            return if (!response.contains("[") && !response.contains("]")) {
                response
            } else {
                println(response + "response")
                TcpConnectionFactory.sendMessage("replaceIfLower $response")
            }
        }

        return "Wrong command name"
    }
}