package com.sharebook.backend.repository

import com.sharebook.backend.entities.NotificationEntity
import com.sharebook.backend.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository


interface NotificationRepository : JpaRepository<NotificationEntity, Long> {

    fun findAllByUserOrderByCreatedAtDesc(userEntity: UserEntity): List<NotificationEntity>

}