package com.threedays.domain.auth.repository

import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.repository.LocationQueryRepository
import java.util.*

class LocationQueryRepositorySpy : LocationQueryRepository,
    AbstractTestRepositorySpy<Location.Id, Location>() {

    override fun findAll(): List<Location> {
        return storage.values.toList()
    }

    override fun find(id: Location.Id): Location? {
        return storage.values.find { it.id == id }
    }

    override fun init() = addSeoulRegions()

    private fun addSeoulRegions() {
        storage.putAll(
            setOf(
                Location.Id(UUID.fromString("191cd0e-a061-7213-b39e-51274c1e7c71")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c71")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("종로구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c72")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c72")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("중구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c73")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c73")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("용산구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c74")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c74")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("성동구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c75")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c75")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("광진구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c76")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c76")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("동대문구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c77")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c77")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("중랑구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c78")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c78")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("성북구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c79")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c79")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("강북구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7a")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7a")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("도봉구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7b")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7b")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("노원구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7c")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7c")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("은평구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7d")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7d")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("서대문구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7e")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7e")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("마포구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7f")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c7f")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("양천구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c80")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c80")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("강서구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c81")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c81")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("구로구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c82")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c82")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("금천구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c83")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c83")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("영등포구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c84")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c84")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("동작구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c85")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c85")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("관악구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c86")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c86")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("서초구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c87")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c87")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("강남구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c88")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c88")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("송파구"),
                ),
                Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c89")) to Location(
                    Location.Id(UUID.fromString("0191cd0e-a061-7213-b39e-51274c1e7c89")),
                    Location.Region("서울특별시"),
                    Location.SubRegion("강동구"),
                ),
            )
        )
    }

}
