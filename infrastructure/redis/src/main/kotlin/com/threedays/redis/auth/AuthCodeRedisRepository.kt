package com.threedays.redis.auth

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthCodeRedisRepository : CrudRepository<AuthCodeRedisHash, String>
