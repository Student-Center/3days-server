package com.threedays.bootstrap.api.user

import com.threedays.application.auth.port.inbound.CommandAuthCode
import com.threedays.application.auth.vo.command.AuthCodeCommand
import com.threedays.domain.support.common.ClientOS
import com.threedays.oas.api.UsersApi
import com.threedays.oas.model.OSType
import com.threedays.oas.model.RequestVerification201Response
import com.threedays.oas.model.RequestVerificationRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val commandAuthCode: CommandAuthCode,
) : UsersApi {

    override fun requestVerification(
        xOSType: OSType,
        requestVerificationRequest: RequestVerificationRequest
    ): ResponseEntity<RequestVerification201Response> {
        return AuthCodeCommand.Create(
            clientOS = ClientOS.valueOf(xOSType.value),
            phoneNumber = requestVerificationRequest.phoneNumber
        ).let { command ->
            commandAuthCode.create(command)
        }.let { authCode ->
            RequestVerification201Response(
                verificationId = authCode.id.value
            )
        }.let {
            ResponseEntity.ok(it)
        }
    }

}
