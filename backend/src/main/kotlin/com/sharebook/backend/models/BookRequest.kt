package com.sharebook.backend.models

import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

data class BookRequest(
    val id: Long = 0,
    val book: Book,
    val user: SafeUser,
    val swapBook: Book?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val approved: Boolean,
    val rejected: Boolean,
)
