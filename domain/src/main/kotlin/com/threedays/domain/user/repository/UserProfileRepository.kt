package com.threedays.domain.user.repository

import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserProfile
import com.threedays.support.common.base.domain.QueryRepository

interface UserProfileRepository : QueryRepository<UserProfile, User.Id>
