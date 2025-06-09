package org.itmo.kotlincollectionmanagerclient.exceptions.handlers

import org.itmo.kotlincollectionmanagerclient.exceptions.AuthenticationException
import org.itmo.kotlincollectionmanagerclient.exceptions.DoubleRecordException
import org.itmo.kotlincollectionmanagerclient.exceptions.ServerNotAvailableException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class BasicExceptionHandler {
    @ExceptionHandler
    fun handlerUnauthorizedException(e: ServerNotAvailableException): ResponseEntity<ErrorMessage?> {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
            ErrorMessage(
                statusCode = HttpStatus.SERVICE_UNAVAILABLE.value(),
                errorName = e.javaClass.simpleName,
                message = e.message ?: "Message not provided"
            )
        )
    }

    @ExceptionHandler
    fun handlerUnauthorizedException(e: AuthenticationException): ResponseEntity<ErrorMessage?> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorMessage(
                statusCode = HttpStatus.UNAUTHORIZED.value(),
                errorName = e.javaClass.simpleName,
                message = e.message ?: "Message not provided"
            )
        )
    }

    @ExceptionHandler
    fun handlerUnauthorizedException(e: DoubleRecordException): ResponseEntity<ErrorMessage?> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorMessage(
                statusCode = HttpStatus.CONFLICT.value(),
                errorName = e.javaClass.simpleName,
                message = e.message ?: "Message not provided"
            )
        )
    }
}