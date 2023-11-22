package com.sharebook.backend.exception

enum class ErrorCode(
    val value: Int
) {
    INVALID_REQUEST_BODY(1),
    USER_NOT_FOUND(2)
}