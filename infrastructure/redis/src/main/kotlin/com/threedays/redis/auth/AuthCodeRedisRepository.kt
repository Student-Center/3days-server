package com.threedays.redis.auth

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.repository.AuthCodeRepository
import com.threedays.domain.auth.vo.AuthCodeId
import org.springframework.stereotype.Component

@Component
class AuthCodeRedisRepository : AuthCodeRepository {

    override fun save(root: AuthCode) {
        TODO("Not yet implemented")
    }

    override fun find(id: AuthCodeId): AuthCode? {
        TODO("Not yet implemented")
    }

    override fun delete(id: AuthCodeId) {
        TODO("Not yet implemented")
    }

    override fun delete(root: AuthCode) {
        TODO("Not yet implemented")
    }


}
