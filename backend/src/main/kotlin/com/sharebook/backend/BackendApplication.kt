package com.sharebook.backend

import com.sharebook.backend.dto.RegisterDto
import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.repository.BookRepository
import com.sharebook.backend.repository.UserRepository
import com.sharebook.backend.services.AuthService
import com.sharebook.backend.utils.JWTUtil
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


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
        bookRepository: BookRepository,
    ): CommandLineRunner {
        return CommandLineRunner { args ->
            authService.register(
                RegisterDto(
                    name = "Farhan",
                    email = "farhan@gmail.com",
                    password = "12345"
                )
            )

            authService.register(
                RegisterDto(
                    name = "Feona",
                    email = "feona@gmail.com",
                    password = "12345"
                )
            )

            val user = userRepository.findByEmail("farhan@gmail.com")
            val user2 = userRepository.findByEmail("feona@gmail.com")


            bookRepository.save(
                BookEntity(
                    name = "Alchemy",
                    authors = listOf("Paulo Coelho", "Random Author"),
                    genre = listOf("Adventure"),
                    coverImage = "",
                    sampleImages = listOf(""),
                    available = true,
                    swappable = true,
                    user = user!!
                )
            )

            bookRepository.save(
                BookEntity(
                    name = "Alchemy 2",
                    authors = listOf("Farhan", "Furqan"),
                    genre = listOf("Horror", "Fantasy"),
                    coverImage = "hello.jpg",
                    sampleImages = listOf(""),
                    available = true,
                    swappable = true,
                    user = user
                )
            )

            bookRepository.save(
                BookEntity(
                    name = "Alchemy 3",
                    authors = listOf("Nick Pirog", "Furqan"),
                    genre = listOf("Horror", "Fantasy"),
                    coverImage = "",
                    sampleImages = listOf(""),
                    available = true,
                    swappable = true,
                    user = user2!!
                )
            )

        }
    }
}

//@Bean
//fun corsConfigurer(): WebMvcConfigurer {
//    return object : WebMvcConfigurer {
//        override fun addCorsMappings(registry: CorsRegistry) {
//            registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//        }
//    }
//}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
