package com.threedays.persistence.user.entity

import com.threedays.domain.user.entity.Location
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(
    name = "locations",
    indexes = [
        Index(name = "idx_region", columnList = "region"),
        Index(name = "idx_sub_region", columnList = "sub_region")
    ]
)
class LocationJpaEntity(
    id: UUID,
    region: String,
    subRegion: String,
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "region", nullable = false, updatable = false)
    var region: String = region
        private set

    @Column(name = "sub_region", nullable = false, updatable = false)
    var subRegion: String = subRegion
        private set

    companion object {

        fun Location.toJpaEntity() = LocationJpaEntity(
            id = id.value,
            region = region.value,
            subRegion = subRegion.value
        )

    }

    fun toDomainEntity() = Location(
        id = Location.Id(id),
        region = Location.Region(region),
        subRegion = Location.SubRegion(subRegion)
    )

}
