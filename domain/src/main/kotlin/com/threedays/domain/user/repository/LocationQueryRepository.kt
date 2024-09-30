package com.threedays.domain.user.repository

import com.threedays.domain.user.entity.Location
import com.threedays.support.common.base.domain.QueryRepository

interface LocationQueryRepository : QueryRepository<Location, Location.Id> {

    fun findAll(): List<Location>

}
