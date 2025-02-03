package com.threedays.domain.auth.repository

import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.user.entity.User
import java.util.concurrent.ConcurrentHashMap

class RefreshTokenRepositorySpy : RefreshTokenRepository {

    private val store: MutableMap<User.Id, RefreshToken> = ConcurrentHashMap()

    override fun save(
        refreshToken: RefreshToken,
        expiresIn: Long,
    ) {
        store[refreshToken.userId] = refreshToken
    }

    override fun find(userId: User.Id): RefreshToken? {
        return store[userId]
    }

    override fun delete(userId: User.Id) {
        store.remove(userId)
    }

    fun clear() {
        store.clear()
    }

}
