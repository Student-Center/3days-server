package com.threedays.domain.user.repository

import com.threedays.domain.user.entity.UserDesiredPartner
import com.threedays.domain.user.vo.UserId
import com.threedays.support.common.base.domain.QueryRepository

interface UserDesiredPartnerRepository : QueryRepository<UserDesiredPartner, UserId>
