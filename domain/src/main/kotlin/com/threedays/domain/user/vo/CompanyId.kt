package com.threedays.domain.user.vo

import com.threedays.support.common.base.domain.UUIDTypeId
import java.util.*

data class CompanyId(override val value: UUID) : UUIDTypeId(value)
