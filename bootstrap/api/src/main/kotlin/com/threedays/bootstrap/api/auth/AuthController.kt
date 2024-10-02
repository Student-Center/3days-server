package com.threedays.bootstrap.api.auth

import com.threedays.application.auth.port.inbound.SendAuthCode
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.common.ClientOS
import com.threedays.oas.api.AuthApi
import com.threedays.oas.model.OSType
import com.threedays.oas.model.SendAuthCodeRequest
import com.threedays.oas.model.SendAuthCodeResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val sendAuthCode: SendAuthCode,
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

        return ResponseEntity.ok(response)
    }

}
