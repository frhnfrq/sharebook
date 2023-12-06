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
        address = address,
        profilePicture = profilePicture
    )
}


fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        password = password,
        address = address,
        profilePicture = profilePicture
    )
}

fun User.toSafeUser(): SafeUser {
    return SafeUser(
        id = id,
        name = name,
        email = email,
        address = address,
        profilePicture = profilePicture
    )
}

fun SafeUser.toUser(password: String): User {
    return User(
        id = id,
        name = name,
        email = email,
        password = password,
        address = address,
        profilePicture = profilePicture,
    )
}