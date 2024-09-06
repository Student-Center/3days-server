package com.threedays.domain.user.entity

import com.threedays.domain.user.vo.LocationId
import com.threedays.support.common.base.domain.DomainEntity

data class Location(
    override val id: LocationId,
    val region: Region,
    val subRegion: SubRegion
) : DomainEntity<Location, LocationId>() {

    @JvmInline
    value class Region(val value: String)

    @JvmInline
    value class SubRegion(val value: String)

    companion object {

        fun of(
            id: LocationId,
            region: String,
            subRegion: String
        ): Location {
            return Location(id, Region(region), SubRegion(subRegion))
        }

    }


}
