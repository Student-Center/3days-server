package com.threedays.application.auth.service

import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.SendAuthCode
import com.threedays.application.auth.port.inbound.VerifyExistingUserAuthCode
import com.threedays.application.auth.port.inbound.VerifyNewUserAuthCode
import com.threedays.application.auth.port.outbound.AuthCodeSmsSender
import com.threedays.domain.auth.entity.AccessToken
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.auth.entity.RegisterToken
import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.repository.AuthCodeRepository
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthCodeService(
    private val userRepository: UserRepository,
    private val authCodeRepository: AuthCodeRepository,
    private val authCodeSmsSender: AuthCodeSmsSender,
    private val authProperties: AuthProperties,
) : SendAuthCode,
    VerifyNewUserAuthCode,
    VerifyExistingUserAuthCode {

    override fun invoke(command: SendAuthCode.Command): SendAuthCode.Result {
        val expireAt: LocalDateTime = LocalDateTime
            .now()
            .plusSeconds(authProperties.authCodeExpirationSeconds)

        val authCode: AuthCode = AuthCode.create(
            clientOS = command.clientOS,
            phoneNumber = command.phoneNumber,
            expireAt = expireAt
        ).also {
            authCodeSmsSender.send(it)
            authCodeRepository.save(it)
        }

        return if (userRepository.findByPhoneNumber(command.phoneNumber) == null) {
            SendAuthCode.Result.NewUser(authCode)
        } else {
            SendAuthCode.Result.ExistingUser(authCode)
        }
    }

    override fun invoke(command: VerifyNewUserAuthCode.Command): VerifyNewUserAuthCode.Result {
        val authCode: AuthCode = authCodeRepository
            .get(command.id)
            .verify(command.code)

        userRepository
            .findByPhoneNumber(authCode.phoneNumber)
            ?.let { throw AuthException.UserExistsException() }

        val registerToken: RegisterToken = RegisterToken.generate(
            secret = authProperties.tokenSecret,
            expirationSeconds = authProperties.registerTokenExpirationSeconds
        )

        return VerifyNewUserAuthCode.Result(
            registerToken = registerToken,
            expiresIn = authProperties.registerTokenExpirationSeconds
        )
    }

    override fun invoke(command: VerifyExistingUserAuthCode.Command): VerifyExistingUserAuthCode.Result {
        val authCode: AuthCode = authCodeRepository
            .get(command.id)
            .verify(command.code)

        val user: User = userRepository.getByPhoneNumber(authCode.phoneNumber)

        val accessToken: AccessToken = AccessToken.generate(
            secret = authProperties.tokenSecret,
            expirationSeconds = authProperties.accessTokenExpirationSeconds,
            userId = user.id
        )

        val refreshToken: RefreshToken = RefreshToken.generate(
            secret = authProperties.tokenSecret,
            expirationSeconds = authProperties.refreshTokenExpirationSeconds,
            userId = user.id
        )

        return VerifyExistingUserAuthCode.Result(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = authProperties.accessTokenExpirationSeconds,
            refreshTokenExpiresIn = authProperties.refreshTokenExpirationSeconds
        )
    }

}
