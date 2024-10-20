package com.threedays.bootstrap.api.support.security

import com.threedays.domain.user.entity.User
import com.threedays.support.common.security.Authentication

data class UserAuthentication(
    val userId: User.Id,
) : Authentication
