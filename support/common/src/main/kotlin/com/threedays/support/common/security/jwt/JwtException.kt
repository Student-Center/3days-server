package com.threedays.support.common.security.jwt

import com.threedays.support.common.base.exception.CustomException

sealed class JwtException(
    codeNumber: Int,
    message: String
) : CustomException(CODE_PREFIX, codeNumber, message) {

    companion object {

        const val CODE_PREFIX = "JWT"

    }

    class DecodeException : JwtException(1, "유효하지 않은 토큰입니다.")
    class Expired : JwtException(2, "토큰이 만료되었습니다.")
    class VerificationException : JwtException(3, "유효하지 않은 토큰입니다.")

}
