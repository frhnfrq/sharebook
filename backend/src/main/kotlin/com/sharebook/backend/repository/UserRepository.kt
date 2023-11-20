package com.sharebook.backend.repository

import com.sharebook.backend.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun existsByEmailIgnoreCase(email: String): Boolean
}
