package com.threedays.domain.user.reposiotry

import com.threedays.domain.user.entity.Company
import com.threedays.domain.user.repository.CompanyQueryRepository

class CompanyQueryRepositorySpy: CompanyQueryRepository {

    private val companies = mutableListOf<Company>()

    fun save(company: Company) {
        companies.add(company)
    }

    fun clear() {
        companies.clear()
    }

    override fun searchCompanies(
        name: String,
        next: Company.Id?,
        limit: Int
    ): Pair<List<Company>, Company.Id?> {
        return companies
            .filter { it.name.contains(name) }
            .let { it.subList(0, limit.coerceAtMost(it.size)) }
            .let { it to it.lastOrNull()?.id }
    }

    override fun find(id: Company.Id): Company? {
        return companies.find { it.id == id }
    }

}
