package com.threedays.domain.user.repository

import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.vo.LocationId
import com.threedays.support.common.base.domain.QueryRepository

interface LocationRepository: QueryRepository<Location, LocationId>
