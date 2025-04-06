package com.threedays.persistence.connection.repository

import com.threedays.persistence.connection.entity.ConnectionAttemptJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ConnectionAttemptJpaRepository : JpaRepository<ConnectionAttemptJpaEntity, UUID>
