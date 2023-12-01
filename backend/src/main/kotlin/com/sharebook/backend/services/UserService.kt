package com.sharebook.backend.services

import com.sharebook.backend.entities.WishEntity
import com.sharebook.backend.exception.CustomException
import com.sharebook.backend.exception.ErrorCode
import com.sharebook.backend.mappers.*
import com.sharebook.backend.models.Book
import com.sharebook.backend.models.User
import com.sharebook.backend.models.Wish
import com.sharebook.backend.repository.UserRepository
import com.sharebook.backend.repository.WishlistRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val wishlistRepository: WishlistRepository,
    private val authService: AuthService,
) {
    fun getUser(authentication: Authentication): Result<User> {
        val userEntity = userRepository.findByEmail(authentication.name)
        return if (userEntity == null) {
            Result.failure(CustomException(errorCode = ErrorCode.USER_NOT_FOUND))
        } else {
            Result.success(userEntity.toUser())
        }
    }

    fun getWishlist(authentication: Authentication): Result<List<Wish>> {
        val userEntity = userRepository.findByEmail(authentication.name)
        return if (userEntity == null) {
            Result.failure(CustomException(errorCode = ErrorCode.USER_NOT_FOUND))
        } else {
            val wishEntities = wishlistRepository.findWishEntitiesByUser(userEntity)
            Result.success(wishEntities.map(WishEntity::toWish))
        }
    }

    fun addToWishlist(authentication: Authentication, wish: Wish): Result<Wish> {
        val userEntity = userRepository.findByEmail(authentication.name)
        return if (userEntity == null) {
            Result.failure(CustomException(errorCode = ErrorCode.USER_NOT_FOUND))
        } else {
            val wishEntity = wishlistRepository.save(wish.toWishEntity(userEntity.toUser()))
            Result.success(wishEntity.toWish())
        }
    }

    fun editWish(authentication: Authentication, wish: Wish): Result<Wish> {
        val user = authService.getUser(authentication)
        val wishEntity = wishlistRepository.save(wish.toWishEntity(user))
        return Result.success(wishEntity.toWish())
    }

    fun deleteWish(authentication: Authentication, id: Long): Result<Boolean> {
        val user = authService.getUser(authentication)
        wishlistRepository.deleteById(id)
        return Result.success(true)
    }
}