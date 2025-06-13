package org.itmo.kotlincollectionmanagerclient.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: Int,
    val message: T,
)
