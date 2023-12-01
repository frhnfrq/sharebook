package com.sharebook.backend.repository

import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.entities.BookRequestEntity
import com.sharebook.backend.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BookRequestRepository : JpaRepository<BookRequestEntity, Long> {

    fun findAllByBookAndApprovedAndRejectedAndIdIsNot(
        bookEntity: BookEntity,
        approved: Boolean,
        rejected: Boolean,
        id: Long
    ): List<BookRequestEntity>

    fun findAllByUser(userEntity: UserEntity): List<BookRequestEntity>

    fun findAllByBook(bookEntity: BookEntity): List<BookRequestEntity>

    fun findByUserAndBookAndRejected(
        userEntity: UserEntity,
        bookEntity: BookEntity,
        rejected: Boolean
    ): BookRequestEntity?

}