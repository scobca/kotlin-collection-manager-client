package org.itmo.kotlincollectionmanagerclient.services

import org.springframework.integration.annotation.ServiceActivator
import org.springframework.stereotype.Service

@Service
class MessageHandlerService {

    @ServiceActivator(inputChannel = "inboundChannel")
    fun handleIncomingMessage(payload: ByteArray): ByteArray {
        val message = String(payload)
        println("Получено сообщение: $message")
        val response = "Ответ на: $message"
        return response.toByteArray()
    }
}