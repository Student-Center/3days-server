package com.threedays.application.auth.port.inbound

interface RefreshLoginTokens {

    fun invoke(command: Command): Result

    data class Command(
        val refreshToken: String
    )

    data class Result(
        val accessToken: String,
        val refreshToken: String,
        val expiresIn: Long
    )
}
