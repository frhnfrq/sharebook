package com.sharebook.backend.security

import com.sharebook.backend.services.AuthService
import com.sharebook.backend.utils.JWTUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authService: AuthService,
    private val jwtUtil: JWTUtil,
    @Lazy private val authenticationManager: AuthenticationManager
) {

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .cors().configurationSource {
                val cors = CorsConfiguration()
                cors.allowedOrigins =
                    listOf("http://localhost:3000", "http://127.0.0.1:3000")
                cors.setAllowedMethods(listOf("GET", "POST", "PUT", "DELETE", "OPTIONS"))
                cors.allowedHeaders = listOf("*")
                return@configurationSource cors
            }
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/login").permitAll()
            .anyRequest().authenticated().and()
            .addFilter(CustomAuthenticationFilter(authenticationManager, jwtUtil))
            .addFilterBefore(
                CustomAuthorizationFilter(authService, jwtUtil),
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }

//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val configuration = CorsConfiguration()
//        configuration.allowedOrigins = mutableListOf("http://localhost:3000/", "http://127.0.0.1:3000/")
//        configuration.setAllowedMethods(mutableListOf("*"))
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//        return source
//    }
}
