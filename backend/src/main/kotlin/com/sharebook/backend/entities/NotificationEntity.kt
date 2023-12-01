package com.sharebook.backend.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class NotificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_entity_gen")
    @SequenceGenerator(name = "notification_entity_gen", sequenceName = "notification_entity_seq")
    val id: Long = 0,
    val description: String,
    val createdAt: LocalDateTime,
    val read: Boolean,
    val route: String,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
)
