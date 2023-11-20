package com.sharebook.backend.dto

data class ResponseDto<T>(
    val success: Boolean,
    val message: String?,
    val data: T?,
) {
    constructor(result: Result<T>) : this(
        success = result.isSuccess,
        message = result.exceptionOrNull()?.message,
        data = result.getOrNull()
    )
}
