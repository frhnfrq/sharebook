package com.sharebook.backend.controllers

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class TestController {

    @GetMapping("test")
    fun test(authentication: Authentication): String {
        return "Hello ${authentication.name}!"
    }
}