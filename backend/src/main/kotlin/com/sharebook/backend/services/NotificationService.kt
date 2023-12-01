package com.sharebook.backend.services

import com.sharebook.backend.entities.NotificationEntity
import com.sharebook.backend.mappers.toNotification
import com.sharebook.backend.mappers.toNotificationEntity
import com.sharebook.backend.mappers.toUserEntity
import com.sharebook.backend.models.Notification
import com.sharebook.backend.repository.NotificationRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val authService: AuthService,
) {

    fun createNotification(authentication: Authentication, description: String, route: String): Notification {
        val user = authService.getUser(authentication)

        val notification = Notification(
            description = description,
            createdAt = LocalDateTime.now(),
            read = false,
            route = route,
        )

        return notificationRepository.save(notification.toNotificationEntity(user)).toNotification()
    }

    fun getNotifications(authentication: Authentication): Result<List<Notification>> {
        val user = authService.getUser(authentication)

        val notificationEntities = notificationRepository.findAllByUserOrderByCreatedAtDesc(user.toUserEntity())

        notificationRepository.saveAll(notificationEntities.filter { !it.read }.map { it.copy(read = true) })

        return Result.success(notificationEntities.map(NotificationEntity::toNotification))
    }
}