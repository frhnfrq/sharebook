package com.sharebook.backend.controllers

import com.sharebook.backend.dto.ResponseDto
import com.sharebook.backend.models.Notification
import com.sharebook.backend.services.NotificationService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/notification")
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping
    fun getNotifications(authentication: Authentication): ResponseDto<List<Notification>> {
        val result = notificationService.getNotifications(authentication)
        return ResponseDto(result)
    }

}