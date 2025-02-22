package com.threedays.bootstrap.api.support.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.threedays.domain.auth.exception.AuthException
import com.threedays.oas.model.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandlingFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {

    companion object {

        private val log = KotlinLogging.logger { }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (exception: AuthException) {
            log.error(exception) { "AuthException occurred" }
            handleAuthException(response, exception)
        }
    }

    private fun handleAuthException(
        response: HttpServletResponse,
        exception: AuthException,
    ) {
        val errorResponse = ErrorResponse(
            code = exception.code,
            message = exception.message,
            time = LocalDateTime.now().toString(),
            type = exception.type,
        )
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType =
            "${MediaType.APPLICATION_JSON_VALUE};charset=${Charsets.UTF_8.name()}"
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

}
