package com.threedays.bootstrap.api.auth

import com.threedays.application.auth.port.inbound.SendAuthCode
import com.threedays.application.auth.port.inbound.VerifyExistingUserAuthCode
import com.threedays.application.auth.port.inbound.VerifyNewUserAuthCode
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.common.ClientOS
import com.threedays.oas.api.AuthApi
import com.threedays.oas.model.ExistingUserVerifyCodeResponse
import com.threedays.oas.model.NewUserVerifyCodeResponse
import com.threedays.oas.model.OSType
import com.threedays.oas.model.SendAuthCodeRequest
import com.threedays.oas.model.SendAuthCodeResponse
import com.threedays.oas.model.VerifyCodeRequest
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AuthController(
    private val sendAuthCode: SendAuthCode,
    private val verifyNewUserAuthCode: VerifyNewUserAuthCode,
    private val verifyExistingUserAuthCode: VerifyExistingUserAuthCode,
) : AuthApi {

    override fun requestVerification(
        xOSType: OSType,
        sendAuthCodeRequest: SendAuthCodeRequest
    ): ResponseEntity<SendAuthCodeResponse> {
        val command = SendAuthCode.Command(
            clientOS = ClientOS.valueOf(xOSType.name),
            phoneNumber = PhoneNumber(sendAuthCodeRequest.phoneNumber),
        )

        val result: SendAuthCode.Result = sendAuthCode.invoke(command)

        val response: SendAuthCodeResponse = when (result) {
            is SendAuthCode.Result.ExistingUser -> SendAuthCodeResponse(
                authCodeId = result.authCode.id.value,
                userStatus = SendAuthCodeResponse.UserStatus.EXISTING
            )

            is SendAuthCode.Result.NewUser -> SendAuthCodeResponse(
                authCodeId = result.authCode.id.value,
                userStatus = SendAuthCodeResponse.UserStatus.NEW
            )
        }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response)
    }

    override fun newUserVerifyCode(
        authCodeId: UUID,
        verifyCodeRequest: VerifyCodeRequest
    ): ResponseEntity<NewUserVerifyCodeResponse> {
        val command = VerifyNewUserAuthCode.Command(
            id = UUIDTypeId.from(authCodeId),
            code = AuthCode.Code(verifyCodeRequest.verificationCode),
        )

        val result: VerifyNewUserAuthCode.Result = verifyNewUserAuthCode.invoke(command)

        val response = NewUserVerifyCodeResponse(
            registerToken = result.registerToken.value,
            expiresIn = result.expiresIn.toInt(),
        )

        return ResponseEntity.ok(response)
    }

    override fun existingUserVerifyCode(
        authCodeId: UUID,
        verifyCodeRequest: VerifyCodeRequest
    ): ResponseEntity<ExistingUserVerifyCodeResponse> {
        val command = VerifyExistingUserAuthCode.Command(
            id = UUIDTypeId.from(authCodeId),
            code = AuthCode.Code(verifyCodeRequest.verificationCode),
        )

        val result: VerifyExistingUserAuthCode.Result = verifyExistingUserAuthCode.invoke(command)

        val response = ExistingUserVerifyCodeResponse(
            accessToken = result.accessToken.value,
            refreshToken = result.refreshToken.value,
            accessTokenExpiresIn = result.accessTokenExpiresIn.toInt(),
            refreshTokenExpiresIn = result.refreshTokenExpiresIn.toInt(),
        )

        return ResponseEntity.ok(response)
    }
}
