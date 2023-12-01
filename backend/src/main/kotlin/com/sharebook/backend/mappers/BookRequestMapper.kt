package com.sharebook.backend.mappers

import com.sharebook.backend.entities.BookRequestEntity
import com.sharebook.backend.models.BookRequest
import com.sharebook.backend.models.User

fun BookRequestEntity.toBookRequest(): BookRequest {
    return BookRequest(
        id = id,
        book = book.toBook(),
        user = user.toUser().toSafeUser(),
        swapBook = swapBook?.toBook(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        approved = approved,
        rejected = rejected,
    )
}

fun BookRequest.toBookRequestEntity(user: User, bookOnRent: Boolean, swapBookOnRent: Boolean): BookRequestEntity {
    return BookRequestEntity(
        id = id,
        book = book.toBookEntity(user, bookOnRent),
        swapBook = swapBook?.toBookEntity(user, swapBookOnRent),
        user = user.toUserEntity(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        approved = approved,
        rejected = rejected,
    )
}