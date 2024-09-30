package com.threedays.domain.auth.repository

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.vo.AuthCodeId

class AuthCodeRepositorySpy : AuthCodeRepository {

    private val authCodes = mutableMapOf<AuthCodeId, AuthCode>()

    override fun save(root: AuthCode) {
        authCodes[root.id] = root
    }

    override fun find(id: AuthCodeId): AuthCode? {
        return authCodes[id]
    }

    override fun delete(id: AuthCodeId) {
        authCodes.remove(id)
    }

    override fun delete(root: AuthCode) {
        authCodes.remove(root.id)
    }

    fun clear() {
        authCodes.clear()
    }

}
