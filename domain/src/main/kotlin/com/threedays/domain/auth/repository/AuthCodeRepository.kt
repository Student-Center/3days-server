package com.threedays.domain.auth.repository

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.vo.AuthCodeId
import com.threedays.support.common.base.domain.Repository

interface AuthCodeRepository : Repository<AuthCode, AuthCodeId>
