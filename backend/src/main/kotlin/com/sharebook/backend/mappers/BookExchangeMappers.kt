package com.sharebook.backend.mappers

import com.sharebook.backend.entities.BookExchangeEntity
import com.sharebook.backend.models.BookExchange
import com.sharebook.backend.models.User

fun BookExchangeEntity.toBookExchange(): BookExchange {
    return BookExchange(
        id = id,
        book = book.toBook(),
        swapBook = swapBook?.toBook(),
        bookOwnerUser = bookOwnerUser.toUser().toSafeUser(),
        bookRenterUser = bookRenterUser.toUser().toSafeUser(),
        createdAt = createdAt,
        dueAt = dueAt,
        returned = returned,
        price = price,
        bookRequest = bookRequest.toBookRequest(),
    )
}

fun BookExchange.toBookExchangeEntity(
    bookOwnerUser: User,
    bookRenterUser: User,
    bookOnRent: Boolean,
    swapBookOnRent: Boolean
): BookExchangeEntity {
    return BookExchangeEntity(
        id = id,
        book = book.toBookEntity(bookOwnerUser, bookOnRent),
        swapBook = swapBook?.toBookEntity(bookRenterUser, swapBookOnRent),
        bookOwnerUser = bookOwnerUser.toUserEntity(),
        bookRenterUser = bookRenterUser.toUserEntity(),
        createdAt = createdAt,
        dueAt = dueAt,
        returned = returned,
        price = price,
        bookRequest = bookRequest.toBookRequestEntity(bookRenterUser, bookOnRent, swapBookOnRent)
    )
}