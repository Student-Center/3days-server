package com.sc.weave2.domain.user.repository

import com.sc.weave2.domain.user.entity.UserProfile
import com.sc.weave2.domain.user.vo.UserId
import com.threedays.support.common.base.domain.QueryRepository

interface UserProfileRepository : QueryRepository<UserProfile, UserId>
