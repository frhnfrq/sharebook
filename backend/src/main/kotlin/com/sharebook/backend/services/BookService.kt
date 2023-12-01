package com.sharebook.backend.services

import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.mappers.toBook
import com.sharebook.backend.mappers.toBookEntity
import com.sharebook.backend.mappers.toUserEntity
import com.sharebook.backend.models.Book
import com.sharebook.backend.repository.BookRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class BookService(
    private val authService: AuthService,
    private val bookRepository: BookRepository
) {

    fun getBooks(authentication: Authentication, query: String?): Result<List<Book>> {
        val user = authService.getUser(authentication)
        val books: MutableList<Book> = mutableListOf()
        if (query != null) {
            books.addAll(bookRepository.searchByQuery(query).map(BookEntity::toBook))
        } else {
            books.addAll(bookRepository.findByUser(user.toUserEntity()).map(BookEntity::toBook))
        }
        return Result.success(books)
    }

    fun createBook(authentication: Authentication, book: Book): Result<Book> {
        val user = authService.getUser(authentication)
        val createdBookEntity = bookRepository.save(book.toBookEntity(user))
        return Result.success(createdBookEntity.toBook())
    }

    fun editBook(authentication: Authentication, book: Book): Result<Book> {
        val user = authService.getUser(authentication)
        val createdBookEntity = bookRepository.save(book.toBookEntity(user))
        return Result.success(createdBookEntity.toBook())
    }

    fun deleteBook(authentication: Authentication, id: Long): Result<Boolean> {
        val user = authService.getUser(authentication)
        bookRepository.deleteById(id)
        return Result.success(true)
    }


}