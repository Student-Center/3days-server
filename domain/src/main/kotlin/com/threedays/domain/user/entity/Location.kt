package com.threedays.domain.user.entity

import com.threedays.support.common.base.domain.DomainEntity
import com.threedays.support.common.base.domain.UUIDTypeId
import java.util.*

data class Location(
    override val id: Id,
    val region: Region,
    val subRegion: SubRegion
) : DomainEntity<Location, Location.Id>() {

    class Id(override val value: UUID) : UUIDTypeId(value)

    @JvmInline
    value class Region(val value: String)

    @JvmInline
    value class SubRegion(val value: String)

    companion object {

        fun of(
            id: Location.Id,
            region: String,
            subRegion: String
        ): Location {
            return Location(id, Region(region), SubRegion(subRegion))
        }

    }


}
