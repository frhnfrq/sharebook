package com.sharebook.backend.controllers

import com.sharebook.backend.dto.ResponseDto
import com.sharebook.backend.mappers.toSafeUser
import com.sharebook.backend.models.SafeUser
import com.sharebook.backend.services.UserService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/user")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getUser(authentication: Authentication): ResponseDto<SafeUser> {
        val result = userService.getUser(authentication)
        return ResponseDto(result.map { it.toSafeUser() })
    }

    @PutMapping
    fun editUser(authentication: Authentication, @RequestBody user: SafeUser): ResponseDto<SafeUser> {
        val result = userService.editUser(authentication, user)
        return ResponseDto(result)
    }

}