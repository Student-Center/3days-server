package com.sc.weave2.domain.user.vo

import com.threedays.support.common.base.domain.TypeId
import java.util.*

data class UserId(override val value: UUID) : TypeId<UUID>(value)
