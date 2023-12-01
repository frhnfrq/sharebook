package com.sharebook.backend.dto

data class CreateBookRequestDto(
    val bookId: Long,
    val swapBookId: Long?,
)
