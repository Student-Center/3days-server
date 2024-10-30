package com.threedays.sms.auth.adapter

import com.threedays.application.auth.port.outbound.AuthCodeSmsSender
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.sms.support.SmsProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!dev")
class AuthCodeSmsSenderCoolSmsAdapter(
    private val smsProperties: SmsProperties,
    private val defaultMessageService: DefaultMessageService,
) : AuthCodeSmsSender {

    companion object {

        private val logger = KotlinLogging.logger { }

    }

    override fun send(authCode: AuthCode) {
        createRequest(authCode)
            .also { sendMessage(it, authCode) }
    }

    private fun createRequest(authCode: AuthCode): SingleMessageSendingRequest {
        return Message(
            from = smsProperties.senderNumber,
            to = authCode.phoneNumber.value,
            text = authCode.getSmsMessage()
        ).let {
            SingleMessageSendingRequest(it)
        }
    }

    private fun sendMessage(
        request: SingleMessageSendingRequest,
        authCode: AuthCode
    ) {
        runCatching {
            defaultMessageService.sendOne(request)
        }.onFailure {
            logger.error(it) { "[Auth Code SMS] Failed to send : ${authCode.phoneNumber.value}, reason : ${it.message}" }
        }.onSuccess {
            logger.debug { "[Auth Code SMS] Sent to : ${authCode.phoneNumber.value}" }
        }
    }

}
