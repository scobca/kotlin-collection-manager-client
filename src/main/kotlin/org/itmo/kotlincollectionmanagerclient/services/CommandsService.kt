package org.itmo.kotlincollectionmanagerclient.services

import kotlinx.serialization.json.Json
import org.itmo.kotlincollectionmanagerclient.api.ApiResponse
import org.itmo.kotlincollectionmanagerclient.api.dto.FlatDto
import org.itmo.kotlincollectionmanagerclient.exceptions.AuthenticationException
import org.itmo.kotlincollectionmanagerclient.exceptions.DoubleRecordException
import org.itmo.kotlincollectionmanagerclient.exceptions.NotFoundException
import org.itmo.kotlincollectionmanagerclient.exceptions.ServerNotAvailableException
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage.getAccessToken
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage.resetUserData
import org.itmo.kotlincollectionmanagerclient.utils.ServerWatcherUtil
import org.itmo.kotlincollectionmanagerclient.utils.TcpConnectionFactory
import org.springframework.stereotype.Service

@Service
class CommandsService(
    private val serverWatcher: ServerWatcherUtil,
    private val tcpConnectionFactory: TcpConnectionFactory
) {
    fun getFlats(): List<FlatDto> {
        val resJson = sendCommand("show ${getAccessToken()}")
        val apiResponse = Json.decodeFromString<ApiResponse<List<FlatDto>>>(resJson)

        return apiResponse.message
    }

    fun insert(body: String) {
        val res = sendCommand("insert $body")
        if (res.contains("message=409")) throw DoubleRecordException("Flat with this id already exists")
    }

    fun update(body: String) {
        val res = sendCommand("update $body")
        if (!res.contains("message=200")) println(res)
    }

    fun clear() {
        sendCommand("clear ${getAccessToken()}")
    }

    fun remove(id: Long) {
        val res = sendCommand("remove $id ${getAccessToken()}")
        println(res)
        if (res.contains("message=404")) throw NotFoundException("Flat with this id does not exist")
    }

    fun removeIfLower(id: Long) {
        val res = sendCommand("removeIfLowerKey $id ${getAccessToken()}")
        println(res)
    }

    fun removeByBalcony(balconyType: Boolean) {
        val res = sendCommand("removeAllByBalcony $balconyType ${getAccessToken()}")
        println(res)
    }

    fun getAveragePrice(): String {
        val resJson = sendCommand("getAveragePrice ${getAccessToken()}")
        val apiResponse = Json.decodeFromString<ApiResponse<String>>(resJson)

        return apiResponse.message
    }

    private fun sendCommand(command: String): String {
        if (serverWatcher.checkConnection()) {
            val response = tcpConnectionFactory.sendMessage(command)
            if (response.contains("message=404")) throw NotFoundException()
            if (response.contains("message=401")) {
                resetUserData()
                throw AuthenticationException("Your session is expired. Login again")
            }

            return response
        }
        throw ServerNotAvailableException()
    }
}