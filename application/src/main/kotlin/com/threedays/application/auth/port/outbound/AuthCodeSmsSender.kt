package com.threedays.application.auth.port.outbound

import com.threedays.domain.auth.entity.AuthCode

interface AuthCodeSmsSender {

    fun send(authCode: AuthCode)

}
