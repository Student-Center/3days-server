package com.threedays.domain.auth.repository

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.support.base.RepositorySpyBase

class AuthCodeRepositorySpy : AuthCodeRepository, RepositorySpyBase<AuthCode, AuthCode.Id>()
