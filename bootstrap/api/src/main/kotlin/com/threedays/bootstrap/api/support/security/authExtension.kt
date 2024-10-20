package com.threedays.bootstrap.api.support.security

import com.threedays.support.common.security.SecurityContext
import com.threedays.support.common.security.SecurityContextHolder

fun <T> withUserAuthentication(block: (UserAuthentication) -> T): T {
    val securityContext: SecurityContext<UserAuthentication>? =
        SecurityContextHolder.getContext()

    val userAuthentication: UserAuthentication = securityContext?.authentication
        ?: throw IllegalStateException("User is not authenticated")

    return block(userAuthentication)
}
