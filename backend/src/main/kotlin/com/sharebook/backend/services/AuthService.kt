package com.sharebook.backend.services

import com.sharebook.backend.dto.RegisterDto
import com.sharebook.backend.entities.UserEntity
import com.sharebook.backend.repository.UserRepository
import com.sharebook.backend.utils.JWTUtil
import org.springframework.security.core.userdetails.User
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
                password = passwordEncoder.encode(registerDto.password)
            )
            userRepository.save(user)

            val accessToken = jwtUtil.create(user.email)

            return Result.success(accessToken)
        }

        return Result.failure(Exception("User already exists"))
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("User not found")

        return User(
            user.email,
            user.password,
            listOf(),
        )
    }

}