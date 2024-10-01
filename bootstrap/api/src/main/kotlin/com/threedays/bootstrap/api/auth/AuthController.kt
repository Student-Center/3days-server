package com.threedays.bootstrap.api.auth

import com.threedays.application.auth.port.inbound.SendAuthCode
import com.threedays.application.auth.port.inbound.VerifyAuthCode
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.support.common.ClientOS
import com.threedays.oas.api.AuthApi
import com.threedays.oas.model.OSType
import com.threedays.oas.model.PhoneNumberRequest
import com.threedays.oas.model.RequestVerificationResponse
import com.threedays.oas.model.VerificationCodeRequest
import com.threedays.oas.model.VerifyCodeResponse
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AuthController(
    private val sendAuthCode: SendAuthCode,
    private val verifyAuthCode: VerifyAuthCode,
) : AuthApi {

    override fun requestVerification(
        xOSType: OSType,
        phoneNumberRequest: PhoneNumberRequest
    ): ResponseEntity<RequestVerificationResponse> {
        val result: AuthCode = SendAuthCode.Command(
            clientOS = ClientOS.valueOf(xOSType.name),
            phoneNumber = phoneNumberRequest.phoneNumber
        ).let {
            sendAuthCode.invoke(it)
        }

        return RequestVerificationResponse(result.id.value)
            .let { ResponseEntity.ok(it) }
    }

    override fun verifyCode(
        verificationId: UUID,
        verificationCodeRequest: VerificationCodeRequest
    ): ResponseEntity<VerifyCodeResponse> {
        val command: VerifyAuthCode.Result = VerifyAuthCode.Command(
            id = UUIDTypeId.from(verificationId),
            code = verificationCodeRequest.verificationCode,
        ).let {
            verifyAuthCode.invoke(it)

        }

        return when (command) {
            is VerifyAuthCode.Result.NewUser -> {
                VerifyCodeResponse(registerToken = command.registerToken.value)
                    .let { ResponseEntity.ok(it) }
            }
            is VerifyAuthCode.Result.ExistsUser -> {
                TODO("사용자가 이미 회원가입한 경우 처리 로직")
            }
        }
    }

}
