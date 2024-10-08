package com.threedays.persistence.user.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.threedays.persistence.user.entity.CompanyJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CompanyJpaRepository : JpaRepository<CompanyJpaEntity, UUID>, KotlinJdslJpqlExecutor
