package org.itmo.kotlincollectionmanagerclient.exceptions

import org.springframework.http.HttpStatus

class DoubleRecordException(message: String) : BasicException(HttpStatus.CONFLICT, message)