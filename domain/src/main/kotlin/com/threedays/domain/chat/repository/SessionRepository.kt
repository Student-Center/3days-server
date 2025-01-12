package com.threedays.domain.chat.repository

import com.threedays.domain.chat.entity.Session
import com.threedays.domain.user.entity.User

interface SessionRepository {

    fun save(session: Session)
    fun findByUserId(userId: User.Id): Session?
    fun deleteById(sessionId: Session.Id)

}
