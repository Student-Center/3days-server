package com.threedays.rest.auth.adapter

import com.threedays.application.auth.port.outbound.AuthCodeSmsSender
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.rest.client.DiscordClient
import com.threedays.rest.support.properties.DiscordProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.net.URI

@Component
@Profile("dev", "local")
class AuthCodeSmsSenderDiscordAdapter(
    private val discordProperties: DiscordProperties,
    private val discordClient: DiscordClient,
) : AuthCodeSmsSender {

    private val uri by lazy {
        URI.create(discordProperties.authSmsHookUrl)
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    override fun send(authCode: AuthCode) {
        logger.info { "[Auth Code SMS(DEV)] Sending to : ${authCode.phoneNumber.value}" }

        val content = """
            Phone Number: ${authCode.phoneNumber.value}
            Message: ${authCode.getSmsMessage()}
        """.trimIndent()

        val request = DiscordClient.Message(content)
        discordClient.send(uri = uri, message = request)
    }

}
