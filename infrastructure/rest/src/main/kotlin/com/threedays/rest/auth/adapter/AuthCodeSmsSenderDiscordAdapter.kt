package com.threedays.rest.auth.adapter

import com.threedays.application.auth.port.outbound.AuthCodeSmsSender
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.rest.auth.client.DiscordClient
import com.threedays.rest.auth.dto.SendDiscordMessageRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class AuthCodeSmsSenderDiscordAdapter(
    private val discordClient: DiscordClient
) : AuthCodeSmsSender {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    override fun send(authCode: AuthCode) {
        logger.info { "[Auth Code SMS(DEV)] Sending to : ${authCode.phoneNumber.value}" }

        val request = SendDiscordMessageRequest(authCode.getSmsMessage())
        discordClient.send(request)
    }

}
