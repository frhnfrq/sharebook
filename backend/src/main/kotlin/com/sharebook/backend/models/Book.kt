package com.sharebook.backend.models

data class Book(
    val id: Long = 0,
    val name: String,
    val authors: List<String>,
    val genre: List<String>,
    val coverImage: String,
    val sampleImages: List<String>,
    val available: Boolean = true,
    val swappable: Boolean = true,
)
