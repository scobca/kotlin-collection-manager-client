package org.itmo.kotlincollectionmanagerclient.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(message: String = "Content not found") : BasicException(HttpStatus.NOT_FOUND, message)