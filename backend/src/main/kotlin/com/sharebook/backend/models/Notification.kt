package com.sharebook.backend.models

import java.time.LocalDateTime

data class Notification(
    val id: Long = 0,
    val description: String,
    val createdAt: LocalDateTime,
    val read: Boolean,
    val route: String,
)
