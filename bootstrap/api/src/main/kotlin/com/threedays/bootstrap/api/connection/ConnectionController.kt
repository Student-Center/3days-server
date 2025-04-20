package com.threedays.bootstrap.api.connection

import com.threedays.application.connection.port.inbound.GetCurrentConnectionAttempt
import com.threedays.bootstrap.api.support.security.withUserAuthentication
import com.threedays.oas.api.ConnectionsApi
import com.threedays.oas.model.GetMyConnectionResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ConnectionController(
    private val getCurrentConnectionAttempt: GetCurrentConnectionAttempt,
) : ConnectionsApi {

    override fun connectionsMyCurrentGet(): ResponseEntity<GetMyConnectionResponse> =
        withUserAuthentication { userAuthentication ->
            val userId = userAuthentication.userId
            val result = getCurrentConnectionAttempt.invoke(
                GetCurrentConnectionAttempt.Command(userId = userId)
            )

            GetMyConnectionResponse(
                connectionAttempt = OASModelAdapter.toOASModel(userId, result.connectionAttempt),
            ).let {
                ResponseEntity.ok(it)
            }
        }

}
