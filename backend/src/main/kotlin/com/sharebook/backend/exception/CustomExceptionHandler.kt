package com.sharebook.backend.exception

import com.sharebook.backend.dto.ResponseDto
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(CustomException::class)
    fun handleAll(ex: CustomException, request: WebRequest?): ResponseEntity<ResponseDto<Unit?>> {
        val responseDto = ResponseDto<Unit?>(
            false, ex.errorCode.name, null,
        )
        return ResponseEntity<ResponseDto<Unit?>>(
            responseDto, HttpHeaders(), HttpStatus.BAD_REQUEST,
        )
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val response = ResponseDto<Unit?>(
            success = false,
            message = ErrorCode.INVALID_REQUEST_BODY.name,
            data = null
        )
        return handleExceptionInternal(
            ex, response, headers, HttpStatus.BAD_REQUEST, request
        )
    }

    //
//    override fun handleMethodArgumentNotValid(
//        ex: MethodArgumentNotValidException,
//        headers: HttpHeaders,
//        status: HttpStatus,
//        request: WebRequest
//    ): ResponseEntity<Any?> {
//        val errors: MutableList<String> = ArrayList()
//        for (error in ex.bindingResult.fieldErrors) {
//            errors.add(error.field + ": " + error.defaultMessage)
//        }
//        for (error in ex.bindingResult.globalErrors) {
//            errors.add(error.objectName + ": " + error.defaultMessage)
//        }
//        val response = ResponseDto<Unit?>(
//            success = false,
//            message = "bad request",
//            data = null
//        )
//        return handleExceptionInternal(
//            ex, response, headers, HttpStatus.BAD_GATEWAY, request
//        )
//    }

}