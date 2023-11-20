package com.sharebook.backend

import com.sharebook.backend.dto.RegisterDto
import com.sharebook.backend.repository.UserRepository
import com.sharebook.backend.services.AuthService
import com.sharebook.backend.utils.JWTUtil
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class BackendApplication {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun getJWTUtil(): JWTUtil {
        return JWTUtil()
    }

    @Bean
    fun run(
        authService: AuthService,
        userRepository: UserRepository,
    ): CommandLineRunner {
        return CommandLineRunner { args ->
            authService.register(
                RegisterDto(
                    name = "Farhan",
                    email = "farhanfarooqui2099@gmail.com",
                    password = "12345"
                )
            )

            val user = userRepository.findByEmail("farhanfarooqui2099@gmail.com")


        }
    }
}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
