package org.itmo.kotlincollectionmanagerclient.utils

import org.itmo.kotlincollectionmanagerclient.validators.InsertValidator
import org.itmo.kotlincollectionmanagerclient.validators.ReplaceIfLowerValidator
import org.itmo.kotlincollectionmanagerclient.validators.UpdateValidator
import org.springframework.stereotype.Component

@Component
class CommandValidatorDistributor(
    private val insertValidator: InsertValidator,
    private val updateValidator: UpdateValidator,
    private val replaceIfLowerValidator: ReplaceIfLowerValidator,
) {
    fun distribute(commandName: String, args: List<String>): Any {
        if (commandName == "insert") return insertValidator.validate(args)
        if (commandName == "update") return updateValidator.validate(args)
        if (commandName == "replaceIfLower") return replaceIfLowerValidator.validate(args)

        return "Wrong command name"
    }
}