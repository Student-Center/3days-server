package com.threedays.application.auth.config

import com.threedays.domain.auth.vo.PhoneNumber
import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.UUID

@ConfigurationProperties(prefix = "auth")
data class AuthProperties(
    val tokenSecret: String,
    val authCodeExpirationSeconds: Long,
    val registerTokenExpirationSeconds: Long,
    val accessTokenExpirationSeconds: Long,
    val refreshTokenExpirationSeconds: Long,
    val tester: List<Tester> = emptyList()
) {

    data class Tester(
        val phoneNumber: String,
        val authCode: String,
        val authCodeId: UUID,
    )

    fun isTesterPhoneNumber(phoneNumber: PhoneNumber): Boolean {
        return tester.any { it.phoneNumber == phoneNumber.value }
    }

    fun getTesterByPhoneNumber(phoneNumber: PhoneNumber): Tester {
        return tester.first { it.phoneNumber == phoneNumber.value }
    }

}
