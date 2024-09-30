package com.threedays.application.auth.port.outbound

import com.threedays.domain.auth.entity.AuthCode
import java.util.concurrent.ConcurrentHashMap

class AuthCodeSmsSenderSpy : AuthCodeSmsSender {

    private val sentAuthCodes = ConcurrentHashMap<AuthCode.Id, AuthCode>()

    override fun send(authCode: AuthCode) {
        sentAuthCodes[authCode.id] = authCode
    }

    fun find(id: AuthCode.Id): AuthCode? {
        return sentAuthCodes[id]
    }

    fun clear() {
        sentAuthCodes.clear()
    }

}
