package org.itmo.kotlincollectionmanagerclient.storages

import kotlin.apply

object TokensStorage {
    private var accessToken: String? = null
    private var refreshToken: String? = null
    private var username: String? = null

    fun getAccessToken(): String? = accessToken
    fun getRefreshToken(): String? = refreshToken
    fun getUsername(): String? = username

    fun setAccessToken(accessToken: String?) = apply { this.accessToken = accessToken }
    fun setRefreshToken(refreshToken: String?) = apply { this.refreshToken = refreshToken }
    fun setUsername(username: String?) = apply { this.username = username }

    fun resetUserData() {
        accessToken = null
        refreshToken = null
        username = null
    }
}