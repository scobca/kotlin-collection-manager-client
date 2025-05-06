package org.itmo.kotlincollectionmanagerclient.validators.interfaces

interface BasicCommandValidator {
    fun validate(args: List<String>): Any
}