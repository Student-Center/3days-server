package com.sc.weave2.domain.user.repository

import com.sc.weave2.domain.user.entity.UserDesiredPartner
import com.sc.weave2.domain.user.vo.UserId
import com.threedays.support.common.base.domain.QueryRepository

interface UserDesiredPartnerRepository: QueryRepository<UserDesiredPartner, UserId>
