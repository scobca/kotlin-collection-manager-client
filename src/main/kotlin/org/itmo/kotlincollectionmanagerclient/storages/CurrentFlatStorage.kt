package org.itmo.kotlincollectionmanagerclient.storages

object CurrentFlatStorage {
    private var flatId: Long? = null

    fun getFlatId(): Long? = flatId
    fun setFlatId(newFlatId: Long) = apply {flatId = newFlatId}
    fun clearFlatStorage() = apply {flatId = null}
}