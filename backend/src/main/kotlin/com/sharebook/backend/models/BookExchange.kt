package com.sharebook.backend.models

import java.time.LocalDateTime

data class BookExchange(
    val id: Long = 0,
    val book: Book,
    val swapBook: Book?,
    val bookOwnerUser: SafeUser,
    val bookRenterUser: SafeUser,
    val createdAt: LocalDateTime,
    val dueAt: LocalDateTime,
    val returned: Boolean = false,
    val price: Int = 0,
    val bookRequest: BookRequest,
)
