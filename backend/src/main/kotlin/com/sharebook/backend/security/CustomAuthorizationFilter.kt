package com.sharebook.backend.security

import com.auth0.jwt.exceptions.JWTVerificationException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sharebook.backend.dto.ResponseDto
import com.sharebook.backend.services.AuthService
import com.sharebook.backend.utils.JWTUtil
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthorizationFilter(
    private val authService: AuthService,
    private val jwtUtil: JWTUtil,
) : OncePerRequestFilter() {

    private val publicEndpoints = listOf(
        "/api/login",
        "/api/register"
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (publicEndpoints.contains(request.servletPath)) {
            filterChain.doFilter(request, response)
        } else {
            val authHeader = request.getHeader(AUTHORIZATION)
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    val token = authHeader.substring("Bearer ".length)

                    val decodedJWT = jwtUtil.verify(token)

                    val username = decodedJWT.subject
                    authService.loadUserByUsername(username)

                    val authenticationToken = UsernamePasswordAuthenticationToken(username, null, null)
                    SecurityContextHolder.getContext().authentication = authenticationToken

                    filterChain.doFilter(request, response)
                } catch (e: Exception) {
                    val errorMessage = when (e) {
                        is JWTVerificationException -> "Invalid token"
                        is UsernameNotFoundException -> "Could not find user"
                        else -> {
                            println(e.message)
                            println(e.printStackTrace())
                            "Something went wrong!"
                        }
                    }
                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    val responseDto = ResponseDto(
                        success = false,
                        message = errorMessage,
                        data = null
                    )

                    jacksonObjectMapper().writeValue(response.outputStream, responseDto)
                }
            } else { // did not provide bearer token
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                val responseDto = ResponseDto(
                    success = false,
                    message = "Invalid or missing authentication token",
                    data = null
                )

                jacksonObjectMapper().writeValue(response.outputStream, responseDto)
            }
        }

    }
}