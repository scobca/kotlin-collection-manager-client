package org.itmo.kotlincollectionmanagerclient.exceptions

import org.springframework.http.HttpStatus

class AuthenticationException(message: String = "Authentication failed") :
    BasicException(HttpStatus.UNAUTHORIZED, message)