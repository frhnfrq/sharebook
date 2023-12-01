package com.sharebook.backend.mappers

import com.sharebook.backend.entities.NotificationEntity
import com.sharebook.backend.models.Notification
import com.sharebook.backend.models.User

fun NotificationEntity.toNotification(): Notification {
    return Notification(
        id = id,
        description = description,
        createdAt = createdAt,
        read = read,
        route = route,
    )
}

fun Notification.toNotificationEntity(user: User): NotificationEntity {
    return NotificationEntity(
        id = id,
        description = description,
        createdAt = createdAt,
        read = read,
        route = route,
        user = user.toUserEntity(),
    )
}