package com.threedays.persistence.user.adapter

import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserDesiredPartner
import com.threedays.domain.user.entity.UserProfile
import com.threedays.domain.user.repository.UserRepository
import com.threedays.domain.user.vo.Company
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.Job
import com.threedays.domain.user.vo.LocationId
import com.threedays.domain.user.vo.UserId
import com.threedays.persistence.user.entity.UserDesiredPartnerEntity
import com.threedays.persistence.user.entity.UserEntity
import com.threedays.persistence.user.entity.UserLocationEntity
import com.threedays.persistence.user.entity.UserProfileEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import java.util.*

@Repository
@Transactional
class UserPersistenceAdapter : UserRepository {

    override fun save(root: User) {
        if (find(root.id) == null) insertUser(root) else updateUser(root)
    }

    @Transactional(readOnly = true)
    override fun find(id: UserId): User? {
        val userData = UserEntity
            .selectAll()
            .where { UserEntity.id eq id.value }
            .singleOrNull() ?: return null

        val profileData = UserProfileEntity
            .selectAll()
            .where { UserProfileEntity.id eq id.value }
            .singleOrNull() ?: return null

        val desiredPartnerData = UserDesiredPartnerEntity
            .selectAll()
            .where { UserDesiredPartnerEntity.id eq id.value }
            .singleOrNull() ?: return null

        val locations = UserLocationEntity
            .selectAll()
            .where { UserLocationEntity.userId eq id.value }
            .map { LocationId(it[UserLocationEntity.locationId].value) }


        return User(
            id = UserId(userData[UserEntity.id].value),
            name = User.Name(userData[UserEntity.name]),
            profile = profileData.toUserProfile(locations),
            desiredPartner = desiredPartnerData.toUserDesiredPartner()
        )
    }

    override fun delete(id: UserId) {
        listOf(
            UserEntity,
            UserDesiredPartnerEntity,
            UserProfileEntity,
        ).forEach { it.deleteWhere { _ -> it.id eq id.value } }
        UserLocationEntity.deleteWhere { userId eq id.value }
    }

    override fun delete(root: User) = delete(root.id)

    private fun insertUser(user: User) {
        val userId = UserEntity.insertAndGetId { it.fromUser(user) }
        UserProfileEntity.insertAndGetId { it.fromUserProfile(user.profile) }
        UserDesiredPartnerEntity.insertAndGetId { it.fromUserDesiredPartner(user.desiredPartner) }
        insertUserLocations(userId.value, user.profile.locationIds)
    }

    private fun updateUser(user: User) {
        UserEntity.update({ UserEntity.id eq user.id.value }) { it.fromUser(user) }
        UserProfileEntity.update({ UserProfileEntity.id eq user.id.value }) {
            it.fromUserProfile(user.profile)
        }
        UserDesiredPartnerEntity.update({ UserDesiredPartnerEntity.id eq user.id.value }) {
            it.fromUserDesiredPartner(user.desiredPartner)
        }
        updateUserLocations(user.id.value, user.profile.locationIds)
    }

    private fun insertUserLocations(
        userId: UUID,
        locations: List<LocationId>
    ) {
        locations.forEach { locationId ->
            UserLocationEntity.insert {
                it[UserLocationEntity.userId] = userId
                it[UserLocationEntity.locationId] = locationId.value
            }
        }
    }

    private fun updateUserLocations(
        userId: UUID,
        locations: List<LocationId>
    ) {
        UserLocationEntity.deleteWhere { UserLocationEntity.userId eq userId }
        insertUserLocations(userId, locations)
    }

    private fun InsertStatement<EntityID<UUID>>.fromUser(user: User) {
        this[UserEntity.id] = user.id.value
        this[UserEntity.name] = user.name.value
    }

    private fun UpdateStatement.fromUser(user: User) {
        this[UserEntity.name] = user.name.value
    }

    private fun InsertStatement<EntityID<UUID>>.fromUserProfile(profile: UserProfile) {
        this[UserProfileEntity.id] = profile.id.value
        this[UserProfileEntity.gender] = profile.gender.name
        this[UserProfileEntity.birthYear] = profile.birthYear.value
        this[UserProfileEntity.company] = profile.company.value
        this[UserProfileEntity.job] = profile.job.value
    }

    private fun UpdateStatement.fromUserProfile(profile: UserProfile) {
        this[UserProfileEntity.gender] = profile.gender.name
        this[UserProfileEntity.birthYear] = profile.birthYear.value
        this[UserProfileEntity.company] = profile.company.value
        this[UserProfileEntity.job] = profile.job.value
    }

    private fun InsertStatement<EntityID<UUID>>.fromUserDesiredPartner(desiredPartner: UserDesiredPartner) {
        this[UserDesiredPartnerEntity.id] = desiredPartner.id.value
        this[UserDesiredPartnerEntity.birthYearRangeStart] =
            desiredPartner.birthYearRange.start.value
        this[UserDesiredPartnerEntity.birthYearRangeEnd] =
            desiredPartner.birthYearRange.endInclusive.value
        this[UserDesiredPartnerEntity.job] = desiredPartner.job.value
        this[UserDesiredPartnerEntity.preferDistance] = desiredPartner.preferDistance.name
    }

    private fun UpdateStatement.fromUserDesiredPartner(desiredPartner: UserDesiredPartner) {
        this[UserDesiredPartnerEntity.birthYearRangeStart] =
            desiredPartner.birthYearRange.start.value
        this[UserDesiredPartnerEntity.birthYearRangeEnd] =
            desiredPartner.birthYearRange.endInclusive.value
        this[UserDesiredPartnerEntity.job] = desiredPartner.job.value
        this[UserDesiredPartnerEntity.preferDistance] = desiredPartner.preferDistance.name
    }

    private fun ResultRow.toUserProfile(locations: List<LocationId>) = UserProfile(
        id = UserId(this[UserProfileEntity.id].value),
        gender = Gender.valueOf(this[UserProfileEntity.gender]),
        birthYear = Year.of(this[UserProfileEntity.birthYear]),
        company = Company(this[UserProfileEntity.company]),
        job = Job(this[UserProfileEntity.job]),
        locationIds = locations
    )

    private fun ResultRow.toUserDesiredPartner() = UserDesiredPartner(
        id = UserId(this[UserDesiredPartnerEntity.id].value),
        birthYearRange = Year.of(this[UserDesiredPartnerEntity.birthYearRangeStart])..Year.of(this[UserDesiredPartnerEntity.birthYearRangeEnd]),
        job = Job(this[UserDesiredPartnerEntity.job]),
        preferDistance = UserDesiredPartner.PreferDistance.valueOf(this[UserDesiredPartnerEntity.preferDistance])
    )
}
