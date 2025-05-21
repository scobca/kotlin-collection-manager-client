package org.itmo.kotlincollectionmanagerclient.services

import org.itmo.kotlincollectionmanagerclient.storages.TokensStorage
import org.itmo.kotlincollectionmanagerclient.utils.ServerWatcherUtil
import org.itmo.kotlincollectionmanagerclient.utils.TcpConnectionFactory
import org.springframework.stereotype.Component
import java.util.Scanner

@Component
class AuthInvoker(
    private val serverWatcher: ServerWatcherUtil,
    private val tcpConnectionFactory: TcpConnectionFactory,
    private val invoker: InvokerService,
) {
    private var runtime = true
    private val authCommands = listOf<String>("login", "register", "refresh")

    fun run() {
        println("Please enter your login and password.")
        println("login <login> <password> — for login")
        println("register <login> <password> — for register")

        while (TokensStorage.getAccessToken() == null && TokensStorage.getRefreshToken() == null && runtime) {
            print("> ")
            val scanner = Scanner(System.`in`)

            val line = scanner.nextLine().trim()
            val command = line.split(" ")[0]

            if (line == "") {
                continue
            }

            if (line == "exit") {
                print("Exiting...")
                runtime = false
                continue
            }

            if (authCommands.contains(command)) {
                if (serverWatcher.checkConnection()) {
                    val response = tcpConnectionFactory.sendMessage(line)
                    val regex = """accessToken=([^,]+), refreshToken=(.+)\)""".toRegex()

                    val matchResult = regex.find(response)

                    val accessToken = matchResult?.groupValues[1]
                    val refreshToken = matchResult?.groupValues[2]

                    if (response.startsWith("UserTokensDto")) {

                        TokensStorage.setAccessToken(accessToken)
                        TokensStorage.setRefreshToken(refreshToken)

                        println("Logged in, welcome!")
                        invoker.run()
                        continue
                    } else {
                        println(response)

                        if (response.contains("message=409 Conflict")) {
                            println("User with this login already exists")
                            continue
                        }
                        if (response.contains("message=401 Unauthorized")) {
                            println("Wrong user login or password")
                            continue
                        }
                    }
                }
            } else {
                println("Unknown command $command, try again.")
                continue
            }
        }
    }
}