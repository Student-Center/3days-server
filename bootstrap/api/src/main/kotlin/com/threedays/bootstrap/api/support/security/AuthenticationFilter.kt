package com.threedays.bootstrap.api.support.security

import com.threedays.application.auth.config.AuthProperties
import com.threedays.domain.auth.entity.AccessToken
import com.threedays.domain.auth.vo.UserAuthentication
import com.threedays.support.common.security.SecurityContext
import com.threedays.support.common.security.SecurityContextHolder
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter(
    private val authProperties: AuthProperties,
) : OncePerRequestFilter() {

    companion object {

        const val AUTH_TOKEN_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            extractToken(request)?.let { setSecurityContext(it) }
            filterChain.doFilter(request, response)
        } finally {
            SecurityContextHolder.clearContext()
        }
    }

    private fun setSecurityContext(token: String) {
        AccessToken
            .verify(token, authProperties.tokenSecret)
            .let { UserAuthentication(it.userId) }
            .let { SecurityContext(it) }
            .let { SecurityContextHolder.setContext(it) }
    }

    private fun extractToken(request: HttpServletRequest): String? {
        return request
            .getHeader(AUTH_TOKEN_HEADER)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.substring(BEARER_PREFIX.length)
            ?.takeIf { it.isNotBlank() }
    }

}
