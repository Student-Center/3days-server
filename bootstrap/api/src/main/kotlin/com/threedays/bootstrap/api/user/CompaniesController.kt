package com.threedays.bootstrap.api.user

import com.threedays.domain.user.repository.CompanyQueryRepository
import com.threedays.oas.api.CompaniesApi
import com.threedays.oas.model.Company
import com.threedays.oas.model.SearchCompaniesResponse
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CompaniesController (
    private val companyQueryRepository: CompanyQueryRepository
): CompaniesApi {

    override fun searchCompanies(
        name: String,
        next: UUID?,
        limit: Int
    ): ResponseEntity<SearchCompaniesResponse> {
        val (resultCompanies, resultNext) = companyQueryRepository.searchCompanies(
            name = name,
            next = next?.let { UUIDTypeId.from(it) },
            limit = limit
        )

        val companyResponse = resultCompanies.map {
            Company(
                id = it.id.value,
                name = it.name
            )
        }

        return ResponseEntity.ok(
            SearchCompaniesResponse(
                companies = companyResponse,
                next = resultNext?.value
            )
        )
    }

}
