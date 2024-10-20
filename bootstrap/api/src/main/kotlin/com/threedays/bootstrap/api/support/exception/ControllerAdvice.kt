package com.threedays.bootstrap.api.support.exception

import com.threedays.oas.model.ErrorResponse
import com.threedays.support.common.base.exception.CustomException
import com.threedays.support.common.exception.NotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime

@RestControllerAdvice
class ControllerAdvice {

    companion object {

        private val logger = KotlinLogging.logger {}
        private const val COMMON_ERROR_TYPE = "COMMON"
        private const val ILLEGAL_ARGUMENT_ERROR_CODE = "1001"
        private const val NOT_FOUND_ERROR_CODE = "1002"
        private const val INTERNAL_SERVER_ERROR_CODE = "1003"

    }

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ErrorResponse> {
        logger.error(e) { "CustomException" }

        val response: ErrorResponse = createErrorResponse(e.type, e.code)

        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, response)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error(e) { e::class.simpleName }

        val (status: HttpStatus, errorCode: String) = when (e) {
            is IllegalArgumentException -> HttpStatus.BAD_REQUEST to ILLEGAL_ARGUMENT_ERROR_CODE
            is NotFoundException -> HttpStatus.NOT_FOUND to NOT_FOUND_ERROR_CODE
            else -> HttpStatus.INTERNAL_SERVER_ERROR to INTERNAL_SERVER_ERROR_CODE
        }

        val response: ErrorResponse = createErrorResponse(COMMON_ERROR_TYPE, errorCode)
        return createResponseEntity(status, response)
    }

    private fun createErrorResponse(
        type: String,
        code: String
    ): ErrorResponse {
        return ErrorResponse(
            time = OffsetDateTime.now(),
            type = type,
            code = code
        )
    }

    private fun createResponseEntity(
        status: HttpStatus,
        response: ErrorResponse
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(status).body(response)
    }

}
