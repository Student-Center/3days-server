package com.threedays.domain.auth.repository

import com.threedays.domain.auth.entity.AuthCode

class AuthCodeRepositorySpy : AuthCodeRepository {

    private val authCodes = mutableMapOf<AuthCode.Id, AuthCode>()

    override fun save(root: AuthCode) {
        authCodes[root.id] = root
    }

    override fun find(id: AuthCode.Id): AuthCode? {
        return authCodes[id]
    }

    override fun delete(id: AuthCode.Id) {
        authCodes.remove(id)
    }

    override fun delete(root: AuthCode) {
        authCodes.remove(root.id)
    }

    fun clear() {
        authCodes.clear()
    }

}
