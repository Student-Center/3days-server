package com.threedays.persistence.user.adapter

import com.threedays.domain.user.entity.User
import com.threedays.domain.user.repository.UserRepository
import com.threedays.persistence.user.entity.UserJpaEntity.Companion.toJpaEntity
import com.threedays.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class UserPersistenceAdapter(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {

    override fun save(root: User) {
        userJpaRepository.save(root.toJpaEntity())
    }

    @Transactional(readOnly = true)
    override fun find(id: User.Id): User? {
        return userJpaRepository
            .findById(id.value)
            .map { it.toDomainEntity() }
            .orElse(null)
    }

    override fun delete(id: User.Id) {
        userJpaRepository.deleteById(id.value)
    }

    override fun delete(root: User) {
        userJpaRepository.delete(root.toJpaEntity())
    }

}
