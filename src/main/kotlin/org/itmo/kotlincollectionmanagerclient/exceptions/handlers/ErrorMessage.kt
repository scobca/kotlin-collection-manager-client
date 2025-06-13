package org.itmo.kotlincollectionmanagerclient.exceptions.handlers

import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(
    val statusCode: Int,
    val errorName: String,
    val message: String,
)