package com.sharebook.backend.models

import javax.persistence.Column

data class Report(
    val id: Long = 0,
    val details: String,
)
