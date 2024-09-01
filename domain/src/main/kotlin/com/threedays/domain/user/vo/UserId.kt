package com.threedays.domain.user.vo

import com.threedays.support.common.base.domain.TypeId
import java.util.*

data class UserId(override val value: UUID) : TypeId<UUID>(value)
