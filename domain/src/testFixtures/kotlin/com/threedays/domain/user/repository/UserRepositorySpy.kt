package com.threedays.domain.user.repository

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.base.RepositorySpyBase
import com.threedays.domain.user.entity.User

class UserRepositorySpy : UserRepository, RepositorySpyBase<User, User.Id>() {

    override fun findByPhoneNumber(phoneNumber: PhoneNumber): User? {
        return super.entities.values.firstOrNull { it.phoneNumber == phoneNumber }
    }

}
