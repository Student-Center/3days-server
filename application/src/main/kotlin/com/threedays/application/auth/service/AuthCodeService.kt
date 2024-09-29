package com.threedays.application.auth.service

import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.CommandAuthCode
import com.threedays.application.auth.port.outbound.AuthCodeSmsSender
import com.threedays.application.auth.vo.command.AuthCodeCommand
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.entity.RegisterToken
import com.threedays.domain.auth.repository.AuthCodeRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthCodeService(
    private val authCodeRepository: AuthCodeRepository,
    private val authCodeSmsSender: AuthCodeSmsSender,
    private val authProperties: AuthProperties,
) : CommandAuthCode {

    override fun create(command: AuthCodeCommand.Create): AuthCode {
        val expireAt: LocalDateTime = LocalDateTime
            .now()
            .plusSeconds(authProperties.authCodeExpirationSeconds)

        return AuthCode.create(
            clientOS = command.clientOS,
            phoneNumber = command.phoneNumber,
            expireAt = expireAt
        ).also {
            authCodeSmsSender.send(it)
            authCodeRepository.save(it)
        }
    }

    override fun verify(command: AuthCodeCommand.Verify): RegisterToken {
        val inputCode: AuthCode.Code = command.code.let { AuthCode.Code(it) }

        authCodeRepository
            .get(command.id)
            .verify(inputCode)

        return RegisterToken.generate(authProperties.tokenSecret)
    }

}
