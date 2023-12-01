package com.sharebook.backend.mappers

import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.models.Book
import com.sharebook.backend.models.User

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        name = name,
        authors = authors,
        genre = genre,
        coverImage = coverImage,
        sampleImages = sampleImages,
        available = available,
        swappable = swappable,
        price = price,
        user = user.toUser().toSafeUser(),
    )
}

fun Book.toBookEntity(user: User): BookEntity {
    return BookEntity(
        id = id,
        name = name,
        authors = authors,
        genre = genre,
        coverImage = coverImage,
        sampleImages = sampleImages,
        available = available,
        swappable = swappable,
        price = price,
        user = user.toUserEntity(),
    )
}