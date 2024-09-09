package com.threedays.domain.auth.vo

import com.threedays.support.common.base.domain.UUIDTypeId
import java.util.*

data class AuthCodeId(override val value: UUID) : UUIDTypeId(value)
