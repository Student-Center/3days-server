package com.threedays.domain.user.repository

import com.threedays.domain.user.entity.Company
import com.threedays.support.common.base.domain.QueryRepository

interface CompanyQueryRepository : QueryRepository<Company, Company.Id>
