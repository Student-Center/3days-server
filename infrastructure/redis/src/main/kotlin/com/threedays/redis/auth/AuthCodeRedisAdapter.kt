package com.threedays.redis.auth

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.repository.AuthCodeRepository
import com.threedays.domain.auth.vo.AuthCodeId
import org.springframework.stereotype.Component

@Component
class AuthCodeRedisAdapter (
    private val authCodeRedisRepository: AuthCodeRedisRepository
): AuthCodeRepository {

    override fun save(root: AuthCode) {
        AuthCodeRedisHash
            .from(root)
            .let { authCodeRedisHash ->
                authCodeRedisRepository.save(authCodeRedisHash)
            }
    }

    override fun find(id: AuthCodeId): AuthCode? {
        return authCodeRedisRepository
            .findById(id.value.toString())
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun delete(id: AuthCodeId) {
        authCodeRedisRepository.deleteById(id.value.toString())
    }

    override fun delete(root: AuthCode) {
        authCodeRedisRepository.deleteById(root.id.value.toString())
    }

}
