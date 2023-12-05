package com.sharebook.backend.repository

import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.entities.BookExchangeEntity
import com.sharebook.backend.entities.UserEntity
import com.sharebook.backend.models.BookExchange
import org.springframework.data.jpa.repository.JpaRepository

interface BookExchangeRepository : JpaRepository<BookExchangeEntity, Long> {
    fun findAllByBook(bookEntity: BookEntity): List<BookExchangeEntity>

    fun findAllByBookOwnerUser(userEntity: UserEntity): List<BookExchangeEntity>

    fun findAllByBookRenterUser(userEntity: UserEntity): List<BookExchangeEntity>
}