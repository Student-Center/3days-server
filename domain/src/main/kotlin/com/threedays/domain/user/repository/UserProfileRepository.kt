package com.threedays.domain.user.repository

import com.threedays.domain.user.entity.UserProfile
import com.threedays.domain.user.vo.UserId
import com.threedays.support.common.base.domain.QueryRepository

interface UserProfileRepository : QueryRepository<UserProfile, UserId>
