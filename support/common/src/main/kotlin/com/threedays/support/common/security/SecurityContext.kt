package com.threedays.support.common.security

class SecurityContext<T: Authentication>(
    val authentication: T,
)
