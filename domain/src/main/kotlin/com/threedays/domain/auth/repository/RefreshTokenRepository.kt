package com.threedays.domain.auth.repository

import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.user.entity.User

interface RefreshTokenRepository {

    fun save(
        refreshToken: RefreshToken,
        expiresIn: Long
    )

    fun find(userId: User.Id): RefreshToken?

    fun delete(userId: User.Id)

}
