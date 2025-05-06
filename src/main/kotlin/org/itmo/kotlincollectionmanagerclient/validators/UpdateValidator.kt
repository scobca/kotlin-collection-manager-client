package org.itmo.kotlincollectionmanagerclient.validators

import org.itmo.kotlincollectionmanagerclient.validators.interfaces.BasicCommandValidator
import org.springframework.stereotype.Component

@Component
class UpdateValidator: BasicCommandValidator {
    override fun validate(args: List<String>) {

    }
}