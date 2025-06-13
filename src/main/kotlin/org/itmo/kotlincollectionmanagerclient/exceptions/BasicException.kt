package org.itmo.kotlincollectionmanagerclient.exceptions

import org.springframework.http.HttpStatus

abstract class BasicException(status: HttpStatus, message: String) : RuntimeException(message)