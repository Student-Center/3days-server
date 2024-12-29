package com.threedays.redis.chat.repository

import com.threedays.redis.chat.hash.SessionRedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRedisRepository : CrudRepository<SessionRedisHash, String> {

    fun findByUserId(userId: String): SessionRedisHash?

}
