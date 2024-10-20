package com.threedays.domain.auth.vo

import com.threedays.domain.user.entity.User
import com.threedays.support.common.security.Authentication

data class UserAuthentication(
    val userId: User.Id,
) : Authentication
