package com.threedays.domain.auth.repository

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.support.common.base.domain.Repository
import com.threedays.support.common.exception.NotFoundException

interface AuthCodeRepository : Repository<AuthCode, AuthCode.Id> {

    fun get(id: AuthCode.Id): AuthCode = find(id) ?: throw NotFoundException("AuthCode not found")

}
