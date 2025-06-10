package org.itmo.kotlincollectionmanagerclient.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(val id: Long, val email: String)