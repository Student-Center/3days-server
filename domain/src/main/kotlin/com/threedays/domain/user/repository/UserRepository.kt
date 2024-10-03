package com.threedays.domain.user.repository

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.Repository
import com.threedays.support.common.exception.NotFoundException

interface UserRepository : Repository<User, User.Id> {

    fun findByPhoneNumber(phoneNumber: PhoneNumber): User?
    fun getByPhoneNumber(phoneNumber: PhoneNumber): User = findByPhoneNumber(phoneNumber)
        ?: throw NotFoundException("User not found by phone number: $phoneNumber")

}
