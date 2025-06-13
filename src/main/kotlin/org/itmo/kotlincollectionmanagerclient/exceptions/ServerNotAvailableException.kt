package org.itmo.kotlincollectionmanagerclient.exceptions

import org.springframework.http.HttpStatus

class ServerNotAvailableException(message: String = "Server not available") :
    BasicException(HttpStatus.SERVICE_UNAVAILABLE, message)