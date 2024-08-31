package com.sc.weave2.domain.user.repository

import com.sc.weave2.domain.user.entity.User
import com.sc.weave2.domain.user.vo.UserId
import com.threedays.support.common.base.domain.Repository

interface UserRepository : Repository<User, UserId>
