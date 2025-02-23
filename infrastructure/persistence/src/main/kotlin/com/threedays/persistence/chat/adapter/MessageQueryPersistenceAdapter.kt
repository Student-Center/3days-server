package com.threedays.persistence.chat.adapter

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.repository.MessageQueryRepository
import com.threedays.persistence.chat.entity.MessageJpaEntity
import com.threedays.persistence.chat.repository.MessageJpaRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class MessageQueryPersistenceAdapter(
    private val entityManager: EntityManager,
    private val jdslRenderContext: RenderContext,
    private val messageJpaRepository: MessageJpaRepository,
) : MessageQueryRepository {

    override fun find(id: Message.Id): Message? {
        return messageJpaRepository
            .findById(id.value)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun scrollByChannelId(
        channelId: Channel.Id,
        next: Message.Id?,
        limit: Int
    ): Pair<List<Message>, Message.Id?> {
        val query: SelectQuery<MessageJpaEntity> = jpql {
            select(
                entity(MessageJpaEntity::class)
            ).from(
                entity(MessageJpaEntity::class)
            ).whereAnd(
                path(MessageJpaEntity::channelId).eq(channelId.value),
                next?.let { path(MessageJpaEntity::id).lessThanOrEqualTo(it.value) }
            ).orderBy(
                path(MessageJpaEntity::createdAt).desc()
            )
        }

        val result: List<Message> = entityManager
            .createQuery(query, jdslRenderContext)
            .apply { maxResults = limit + 1 }
            .resultList
            .map { it.toDomain() }

        val hasNextPage: Boolean = result.size > limit
        val nextId: Message.Id? = if (hasNextPage) {
            result[limit].id
        } else {
            null
        }

        return result.take(limit) to nextId
    }
}
