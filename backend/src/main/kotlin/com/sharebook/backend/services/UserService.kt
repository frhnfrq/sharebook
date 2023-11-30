package com.sharebook.backend.services

import com.sharebook.backend.exception.CustomException
import com.sharebook.backend.exception.ErrorCode
import com.sharebook.backend.mappers.toUser
import com.sharebook.backend.models.User
import com.sharebook.backend.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getUser(authentication: Authentication): Result<User> {
        val userEntity = userRepository.findByEmail(authentication.name)
        return if (userEntity == null) {
            Result.failure(CustomException(errorCode = ErrorCode.USER_NOT_FOUND))
        } else {
            Result.success(userEntity.toUser())
        }
    }
}