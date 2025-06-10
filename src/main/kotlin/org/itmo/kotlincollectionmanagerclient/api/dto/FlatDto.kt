package org.itmo.kotlincollectionmanagerclient.api.dto

import kotlinx.serialization.Serializable
import org.itmo.kotlincollectionmanagerclient.collection.Furnish

@Serializable
data class FlatDto(
    val id: Long,
    val name: String,
    val coordinates: CoordinatesDto,
    val area: Long,
    val numberOfRooms: Long,
    val price: Long,
    val balcony: Boolean,
    val furnish: Furnish,
    val house: HouseDto,
    val user: UserDto,
)
