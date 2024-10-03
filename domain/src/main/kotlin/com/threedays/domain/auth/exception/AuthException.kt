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

    /**
     * 가입 된 사용자에 대해 인증 코드 인증을 요청할때
     * 이미 가입된 사용자라면 발생하는 예외
     */
    data class UserExistsException(
        override val message: String = "이미 가입된 사용자입니다.",
    ) : AuthException(1005, message)

    /**
     * 가입 된 사용자에 대해 인증 코드 인증을 요청할때
     * 가입되지 않은 사용자라면 발생하는 예외
     */
    data class UserNotExistsException(
        override val message: String = "가입되지 않은 사용자입니다.",
    ) : AuthException(1006, message)

    data class AccessTokenExpiredException(
        override val message: String = "Access Token이 만료되었습니다.",
    ) : AuthException(1007, message)

    data class InvalidAccessTokenException(
        override val message: String = "유효하지 않은 Access Token입니다.",
    ) : AuthException(1008, message)

    data class RefreshTokenExpiredException(
        override val message: String = "Refresh Token이 만료되었습니다.",
    ) : AuthException(1009, message)

    data class InvalidRefreshTokenException(
        override val message: String = "유효하지 않은 Refresh Token입니다.",
    ) : AuthException(1010, message)

}
