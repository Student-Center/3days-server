package com.threedays.domain.auth.repository

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.vo.AuthCodeId
import com.threedays.support.common.base.domain.Repository
import com.threedays.support.common.exception.NotFoundException

interface AuthCodeRepository : Repository<AuthCode, AuthCodeId> {

    fun get(id: AuthCodeId): AuthCode = find(id) ?: throw NotFoundException("AuthCode not found")

}
