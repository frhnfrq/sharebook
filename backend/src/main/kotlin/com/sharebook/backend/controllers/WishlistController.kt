package com.sharebook.backend.controllers

import com.sharebook.backend.dto.ResponseDto
import com.sharebook.backend.models.Wish
import com.sharebook.backend.services.UserService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

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

    @PutMapping
    fun editWish(authentication: Authentication, @RequestBody wish: Wish): ResponseDto<Wish> {
        val result = userService.editWish(authentication, wish)
        return ResponseDto(result)
    }

    @DeleteMapping("{id}")
    fun deleteWish(authentication: Authentication, @PathVariable("id") id: Long): ResponseDto<Boolean> {
        val result = userService.deleteWish(authentication, id)
        return ResponseDto(result)
    }

    

}