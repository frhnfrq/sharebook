package com.sharebook.backend.controllers

import com.sharebook.backend.dto.ResponseDto
import com.sharebook.backend.mappers.toSafeUser
import com.sharebook.backend.models.SafeUser
import com.sharebook.backend.models.Wish
import com.sharebook.backend.services.UserService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/wishlist")
class WishlistController(
    private val userService: UserService,
) {

    @GetMapping
    fun getWishlist(authentication: Authentication): ResponseDto<List<Wish>> {
        val result = userService.getWishlist(authentication)
        return ResponseDto(result)
    }

    @PostMapping
    fun addToWishlist(authentication: Authentication, @RequestBody wish: Wish): ResponseDto<Wish> {
        val result = userService.addToWishlist(authentication, wish)
        return ResponseDto(result)
    }

}