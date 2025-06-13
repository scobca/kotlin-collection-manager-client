package org.itmo.kotlincollectionmanagerclient.services

import org.itmo.kotlincollectionmanagerclient.exceptions.AuthenticationException
import org.itmo.kotlincollectionmanagerclient.exceptions.DoubleRecordException
import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage
import org.itmo.kotlincollectionmanagerclient.utils.ServerWatcherUtil
import org.itmo.kotlincollectionmanagerclient.utils.TcpConnectionFactory
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val serverWatcher: ServerWatcherUtil,
    private val tcpConnectionFactory: TcpConnectionFactory,
) {
    fun login(login: String, password: String): Boolean = sendCommand("login", login, password)

    fun register(login: String, password: String): Boolean = sendCommand("register", login, password)

    private fun sendCommand(command: String, login: String, password: String): Boolean {
        if (serverWatcher.checkConnection()) {
            val line = "$command $login $password"
            val response = tcpConnectionFactory.sendMessage(line)
            val regex = """accessToken=([^,]+), refreshToken=(.+)\)""".toRegex()

            val matchResult = regex.find(response)

            val accessToken = matchResult?.groupValues[1]
            val refreshToken = matchResult?.groupValues[2]

            if (response.startsWith("UserTokensDto")) {
                TokensStorage.setAccessToken(accessToken)
                TokensStorage.setRefreshToken(refreshToken)

                return true
            } else {
                if (response.contains("message=401") || response.contains("message=404")) {
                    throw AuthenticationException("Username or password invalid")
                }
                if (response.contains("message=409")) {
                    throw DoubleRecordException("User with this email already exists")
                }
            }
        }
        return false
    }
}