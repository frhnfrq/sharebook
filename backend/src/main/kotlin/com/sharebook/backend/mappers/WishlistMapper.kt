package com.sharebook.backend.mappers

import com.sharebook.backend.entities.WishEntity
import com.sharebook.backend.models.User
import com.sharebook.backend.models.Wish

fun WishEntity.toWish(): Wish {
    return Wish(
        id = id,
        name = name,
        authors = authors
    )
}

fun Wish.toWishEntity(user: User): WishEntity {
    return WishEntity(
        id = id,
        name = name,
        authors = authors,
        user = user.toUserEntity(),
    )
}