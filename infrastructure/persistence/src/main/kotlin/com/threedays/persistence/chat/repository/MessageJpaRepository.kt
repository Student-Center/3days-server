package com.threedays.persistence.chat.repository

import com.threedays.persistence.chat.entity.MessageJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageJpaRepository : JpaRepository<MessageJpaEntity, UUID>
