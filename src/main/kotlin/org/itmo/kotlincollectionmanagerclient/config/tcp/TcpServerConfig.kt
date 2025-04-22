package org.itmo.kotlincollectionmanagerclient.config.tcp

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.ip.tcp.TcpInboundGateway
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory
import org.springframework.integration.ip.tcp.serializer.TcpCodecs

@Configuration
class TcpServerConfig(private val tcpChannels: TcpChannels) {
    private val dotenv: Dotenv = Dotenv.load()

    @Bean
    fun serverConnectionFactory(): TcpNetServerConnectionFactory =
        TcpNetServerConnectionFactory(dotenv.get("TCP_SERVER_PORT").toInt()).apply {
            serializer = TcpCodecs.lengthHeader(4)
            deserializer = TcpCodecs.lengthHeader(4)
        }

    @Bean
    fun tcpInboundGateway(): TcpInboundGateway = TcpInboundGateway().apply {
        setConnectionFactory(serverConnectionFactory())
        setRequestChannel(tcpChannels.inboundChannel())
        setReplyChannel(tcpChannels.outboundChannel())
    }
}