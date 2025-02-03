package com.threedays.redis.auth

import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.auth.repository.RefreshTokenRepository
import com.threedays.domain.user.entity.User
import org.springframework.stereotype.Component

@Component
class RefreshTokenRedisAdapter(
    private val refreshTokenRedisRepository: RefreshTokenRedisRepository
) : RefreshTokenRepository {

    override fun save(
        refreshToken: RefreshToken,
        expiresIn: Long
    ) {
        RefreshTokenRedisHash
            .of(refreshToken, expiresIn)
            .let { refreshTokenRedisRepository.save(it) }
    }

    override fun find(userId: User.Id): RefreshToken? {
        return refreshTokenRedisRepository
            .findById(userId.value.toString())
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun delete(userId: User.Id) {
        refreshTokenRedisRepository.deleteById(userId.value.toString())
    }

}
