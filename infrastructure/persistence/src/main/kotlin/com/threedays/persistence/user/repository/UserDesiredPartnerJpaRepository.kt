package com.threedays.persistence.user.repository

import com.threedays.persistence.user.entity.UserDesiredPartnerJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDesiredPartnerJpaRepository : JpaRepository<UserDesiredPartnerJpaEntity, UUID>
