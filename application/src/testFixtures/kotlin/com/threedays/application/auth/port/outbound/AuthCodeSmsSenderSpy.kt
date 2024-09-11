package com.threedays.application.auth.port.outbound

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.vo.AuthCodeId
import java.util.concurrent.ConcurrentHashMap

class AuthCodeSmsSenderSpy : AuthCodeSmsSender {

    private val sentAuthCodes = ConcurrentHashMap<AuthCodeId, AuthCode>()

    override fun send(authCode: AuthCode) {
        sentAuthCodes[authCode.id] = authCode
    }

    fun find(id: AuthCodeId): AuthCode? {
        return sentAuthCodes[id]
    }

    fun clear() {
        sentAuthCodes.clear()
    }

}
