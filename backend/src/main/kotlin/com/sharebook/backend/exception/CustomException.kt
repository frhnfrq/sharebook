package com.sharebook.backend.exception

class CustomException(
    val errorCode: ErrorCode
) : Exception()