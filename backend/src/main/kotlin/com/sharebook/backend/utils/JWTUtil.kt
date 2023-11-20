package com.sharebook.backend.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.*

class JWTUtil {
    private val secret: String = "secret"
    private val algorithm: Algorithm = Algorithm.HMAC256(secret.toByteArray())
    private val verifier: JWTVerifier = JWT.require(algorithm).build()

    fun verify(token: String): DecodedJWT {
        return verifier.verify(token)
    }

    fun create(subject: String): String {
        return JWT.create()
            .withSubject(subject)
            .withIssuer("ShareBook")
            .withIssuedAt(Date())
            .sign(algorithm)
    }
}

