package org.itmo.kotlincollectionmanagerclient.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class HouseDto(val id: Long, val name: String, val year: Int, val numberOfFloors: Long)