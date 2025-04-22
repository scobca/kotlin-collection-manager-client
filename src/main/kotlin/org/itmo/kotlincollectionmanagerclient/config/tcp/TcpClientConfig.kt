package org.itmo.kotlincollectionmanagerclient.config.tcp

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.ip.tcp.TcpOutboundGateway
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory
import org.springframework.integration.ip.tcp.serializer.TcpCodecs

@Configuration
class TcpClientConfig(private val tcpChannels: TcpChannels) {
    private val dotenv: Dotenv = Dotenv.load()

    @Bean
    fun clientConnectionFactory(): TcpNetClientConnectionFactory =
        TcpNetClientConnectionFactory(dotenv.get("TCP_SERVER_HOST"), dotenv.get("TCP_SERVER_PORT").toInt()).apply {
            serializer = TcpCodecs.lengthHeader(4)
            deserializer = TcpCodecs.lengthHeader(4)
        }

    @Bean
    @ServiceActivator(inputChannel = "outboundChannel")
    fun tcpOutboundGateway(clientConnectionFactory: TcpNetClientConnectionFactory): TcpOutboundGateway =
        TcpOutboundGateway().apply {
            setConnectionFactory(clientConnectionFactory)
            setReplyChannel(tcpChannels.inboundChannel())
        }
}