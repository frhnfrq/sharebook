package com.sharebook.backend.services

import com.sharebook.backend.dto.RegisterDto
import com.sharebook.backend.entities.UserEntity
import com.sharebook.backend.exception.CustomException
import com.sharebook.backend.exception.ErrorCode
import com.sharebook.backend.mappers.toUser
import com.sharebook.backend.models.User
import com.sharebook.backend.repository.UserRepository
import com.sharebook.backend.utils.JWTUtil
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User as CoreUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JWTUtil,
) : UserDetailsService {

    fun register(registerDto: RegisterDto): Result<String> {
        if (!userRepository.existsByEmailIgnoreCase(registerDto.email)) {
            val user = UserEntity(
                name = registerDto.name,
                email = registerDto.email,
                password = passwordEncoder.encode(registerDto.password),
                address = null,
                profilePicture = null,
            )
            userRepository.save(user)

            val accessToken = jwtUtil.create(user.email)

            return Result.success(accessToken)
        }

        return Result.failure(Exception("User already exists"))
    }

    fun getUser(authentication: Authentication): User {
        val userEntity = userRepository.findByEmail(authentication.name)

        if (userEntity == null) {
            throw CustomException(ErrorCode.USER_NOT_FOUND)
        } else {
            return userEntity.toUser()
        }
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("User not found")

        return CoreUser(
            user.email,
            user.password,
            listOf(),
        )
    }

}