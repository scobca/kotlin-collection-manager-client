package org.itmo.kotlincollectionmanagerclient.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatesDto(val id: Long, val x: Long, val y: Float) {}
