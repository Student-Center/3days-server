package com.threedays.domain.user.repository

import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserDesiredPartner
import com.threedays.support.common.base.domain.QueryRepository

interface UserDesiredPartnerRepository : QueryRepository<UserDesiredPartner, User.Id>
