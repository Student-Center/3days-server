package com.threedays.persistence.user.repository

import com.threedays.persistence.user.entity.JobJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobJpaRepository : JpaRepository<JobJpaEntity, UUID>
