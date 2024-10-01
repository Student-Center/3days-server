package com.threedays.application.auth.service

import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.SendAuthCode
import com.threedays.application.auth.port.inbound.VerifyAuthCode
import com.threedays.application.auth.port.outbound.AuthCodeSmsSender
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
) : SendAuthCode,
    VerifyAuthCode {

    override fun invoke(command: SendAuthCode.Command): AuthCode {
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

    override fun invoke(command: VerifyAuthCode.Command): VerifyAuthCode.Result {
        val inputCode: AuthCode.Code = command.code.let { AuthCode.Code(it) }

        authCodeRepository
            .get(command.id)
            .verify(inputCode)

        // 기존에 가입한 유저일 경우 분기 처리

        return RegisterToken
            .generate(authProperties.tokenSecret)
            .let { VerifyAuthCode.Result.NewUser(it) }
    }

}
