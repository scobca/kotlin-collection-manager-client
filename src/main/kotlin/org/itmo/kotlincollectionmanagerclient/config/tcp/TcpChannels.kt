package org.itmo.kotlincollectionmanagerclient.config.tcp

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.DirectChannel
import org.springframework.messaging.MessageChannel

@Configuration
class TcpChannels {
    @Bean
    fun inboundChannel(): MessageChannel = DirectChannel()

    @Bean
    fun outboundChannel(): MessageChannel = DirectChannel()
}