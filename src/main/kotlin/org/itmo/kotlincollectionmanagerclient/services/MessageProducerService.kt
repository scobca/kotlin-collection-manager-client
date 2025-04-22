package org.itmo.kotlincollectionmanagerclient.services

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandlingException
import org.springframework.messaging.support.GenericMessage
import org.springframework.stereotype.Service

@Service
class MessageProducerService(@Qualifier("outboundChannel") private val outboundGateway: MessageChannel) {

    fun sendMessage(message: String) {
        val msg = GenericMessage(message.toByteArray())

        try {
            outboundGateway.send(msg)
        } catch (_: MessageHandlingException) {
            println("Server not available. Try again later.")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}