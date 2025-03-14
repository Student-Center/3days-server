package com.threedays.persistence.chat.repository

import com.threedays.persistence.chat.entity.ChannelJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChannelJpaRepository: JpaRepository<ChannelJpaEntity, UUID>
