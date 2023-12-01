package com.sharebook.backend.repository

import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface BookRepository : JpaRepository<BookEntity, Long> {
    fun findByNameContains(name: String): List<BookEntity>
    fun findByUser(user: UserEntity): List<BookEntity>

    @Query(
        "SELECT b FROM BookEntity b WHERE " +
                "LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                "EXISTS (SELECT LOWER(author) FROM b.authors author WHERE LOWER(author) LIKE LOWER(CONCAT('%', :query, '%'))) OR " +
                "EXISTS (SELECT LOWER(genre) FROM b.genre genre WHERE LOWER(genre) LIKE LOWER(CONCAT('%', :query, '%')))"
    )
    fun searchByQuery(query: String): List<BookEntity>

}
