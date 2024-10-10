package com.threedays.domain.user.repository

import com.threedays.domain.user.entity.Location
import com.threedays.support.common.base.domain.QueryRepository

interface LocationQueryRepository : QueryRepository<Location, Location.Id> {

    /**
     * 지역명(시,도) 목록을 조회합니다.
     * @return 지역명 목록
     */
    fun getLocationRegions(): List<String>

    /**
     * 지역명(시,도)으로 지역 목록을 조회합니다.
     * @param regionName 지역명
     * @return 지역 목록
     */
    fun getLocationsByRegion(regionName: String): List<Location>

}
