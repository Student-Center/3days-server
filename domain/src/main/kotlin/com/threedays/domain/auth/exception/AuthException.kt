package com.threedays.domain.auth.exception

import com.threedays.support.common.base.exception.CustomException

sealed class AuthException(
    codeNumber: Int,
    override val message: String = DEFAULT_MESSAGE,
) : CustomException("Auth", codeNumber, message) {

    data class AuthCodeExpiredException(
        override val message: String = "인증 코드가 만료되었습니다.",
    ) : AuthException(1001, message)

    data class InvalidAuthCodeException(
        override val message: String = "유효하지 않은 인증 코드입니다.",
    ) : AuthException(1002, message)

    data class RegisterTokenExpiredException(
        override val message: String = "회원 가입 토큰이 만료되었습니다.",
    ) : AuthException(1003, message)

    data class InvalidRegisterTokenException(
        override val message: String = "유효하지 않은 회원 가입 토큰입니다.",
    ) : AuthException(1004, message)

}
