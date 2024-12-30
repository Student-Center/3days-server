package com.threedays.persistence.chat.adapter

import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.repository.MessageRepository
import com.threedays.persistence.chat.entity.MessageJpaEntity
import com.threedays.persistence.chat.repository.MessageJpaRepository
import org.springframework.stereotype.Repository

@Repository
class MessagePersistenceAdapter(
    private val messageJpaRepository: MessageJpaRepository,
) : MessageRepository {

    override fun save(message: Message) {
        val messageJpaEntity: MessageJpaEntity = MessageJpaEntity.from(message)
        messageJpaRepository.save(messageJpaEntity)
    }

}
