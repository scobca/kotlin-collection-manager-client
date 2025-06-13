package org.itmo.kotlincollectionmanagerclient.storages

import org.itmo.kotlincollectionmanagerclient.api.dto.FlatDto

object FlatsStorage {
    private val flats: MutableList<FlatDto> = mutableListOf()

    fun setFlatsCollection(flats: List<FlatDto>) {
        this.flats.clear()
        this.flats.addAll(flats)
    }

    fun getFlatsCollection(): List<FlatDto> = flats
}