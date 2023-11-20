package com.sharebook.backend.controllers

import com.sharebook.backend.dto.RegisterDto
import com.sharebook.backend.dto.ResponseDto
import com.sharebook.backend.services.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("register")
    fun register(@RequestBody registerDto: RegisterDto): ResponseDto<String> {
        val result = authService.register(registerDto)
        return ResponseDto(result)
    }
}