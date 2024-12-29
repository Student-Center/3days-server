package com.threedays.redis.chat.adapter

import com.threedays.domain.chat.entity.Session
import com.threedays.domain.chat.repository.SessionRepository
import com.threedays.domain.user.entity.User
import com.threedays.redis.chat.hash.SessionRedisHash.Companion.toRedisHash
import com.threedays.redis.chat.repository.SessionRedisRepository
import org.springframework.stereotype.Repository

@Repository
class SessionRepositoryRedisAdapter(
    private val sessionRedisRepository: SessionRedisRepository,
) : SessionRepository {

    override fun save(session: Session) {
        sessionRedisRepository.save(session.toRedisHash())
    }

    override fun findByUserId(userId: User.Id): Session? {
        return sessionRedisRepository
            .findByUserId(userId.toString())
            ?.toDomain()
    }

}
