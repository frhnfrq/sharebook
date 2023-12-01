package com.sharebook.backend.models

data class Wish(
    val id: Long = 0,
    val name: String,
    val authors: List<String>,
)
