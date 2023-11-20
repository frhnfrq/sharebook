package com.sharebook.backend.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sharebook.backend.dto.LoginDto
import com.sharebook.backend.dto.ResponseDto
import com.sharebook.backend.utils.JWTUtil
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFilter(
    private val authManager: AuthenticationManager,
    private val jwtUtil: JWTUtil
) : UsernamePasswordAuthenticationFilter() {

    init {
        setFilterProcessesUrl("/api/login")
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val loginDto = jacksonObjectMapper().readValue(request.inputStream, LoginDto::class.java)
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)
        return authManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val user: User = authResult.principal as User

        val accessToken = jwtUtil.create(
            subject = user.username,
        )

        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val responseDto = ResponseDto(
            success = true,
            message = null,
            data = accessToken
        )

        jacksonObjectMapper().writeValue(response.outputStream, responseDto)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val responseDto = ResponseDto(
            success = false,
            message = failed.message,
            data = null
        )

        jacksonObjectMapper().writeValue(response.outputStream, responseDto)
    }
}