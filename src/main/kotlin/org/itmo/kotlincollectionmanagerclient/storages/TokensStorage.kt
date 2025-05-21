package org.itmo.kotlincollectionmanagerclient.storages

import kotlin.apply

object TokensStorage {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    fun getAccessToken(): String? = accessToken
    fun getRefreshToken(): String? = refreshToken

    fun setAccessToken(accessToken: String?) = apply { this.accessToken = accessToken }
    fun setRefreshToken(refreshToken: String?) = apply { this.refreshToken = refreshToken }
}