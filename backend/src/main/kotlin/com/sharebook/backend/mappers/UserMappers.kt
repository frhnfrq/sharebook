package com.sharebook.backend.mappers

import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.entities.UserEntity
import com.sharebook.backend.models.Book
import com.sharebook.backend.models.SafeUser
import com.sharebook.backend.models.User

fun UserEntity.toUser(): User {
    return User(
        id = id,
        name = name,
        email = email,
        password = password,
    )
}


fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        password = password,
    )
}

fun User.toSafeUser(): SafeUser {
    return SafeUser(
        id = id,
        name = name,
        email = email,
    )
}