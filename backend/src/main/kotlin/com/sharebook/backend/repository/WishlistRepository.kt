package com.sharebook.backend.repository

import com.sharebook.backend.entities.UserEntity
import com.sharebook.backend.entities.WishEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WishlistRepository : JpaRepository<WishEntity, Long> {

    fun findWishEntitiesByUser(user: UserEntity): List<WishEntity>

}