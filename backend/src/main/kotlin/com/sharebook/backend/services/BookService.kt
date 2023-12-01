package com.sharebook.backend.services

import com.sharebook.backend.dto.CreateBookRequestDto
import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.mappers.*
import com.sharebook.backend.models.Book
import com.sharebook.backend.models.BookRequest
import com.sharebook.backend.repository.BookRepository
import com.sharebook.backend.repository.BookRequestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class BookService(
    private val authService: AuthService,
    private val bookRepository: BookRepository,
    private val bookRequestRepository: BookRequestRepository,
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

    fun createBookRequest(
        authentication: Authentication,
        createBookRequestDto: CreateBookRequestDto
    ): Result<BookRequest> {
        val user = authService.getUser(authentication)
        val bookEntity = bookRepository.findById(createBookRequestDto.bookId).get()
        val swapBookEntity = bookRepository.findByIdOrNull(createBookRequestDto.swapBookId)

        val previousRequest = bookRequestRepository.findByUserAndBookAndRejected(user.toUserEntity(), bookEntity, false)

        if (previousRequest != null) {
            return Result.failure(Exception("You have already requested for this book"))
        }

        val bookRequest = BookRequest(
            book = bookEntity.toBook(),
            user = user.toSafeUser(),
            swapBook = swapBookEntity?.toBook(),
            createdAt = null,
            updatedAt = null,
            approved = false,
            rejected = false,
        )
        val bookRequestEntity = bookRequestRepository.save(bookRequest.toBookRequestEntity(user))

        return Result.success(bookRequestEntity.toBookRequest())
    }

    fun approveBookRequest(
        authentication: Authentication,
        bookRequestId: Long,
    ): Result<Boolean> {
        val user = authService.getUser(authentication)
        val bookRequestEntity = bookRequestRepository.findById(bookRequestId).get()
        if (bookRequestEntity.book.user.id != user.id) {
            return Result.failure(Exception("You are not the owner of this book"))
        }

        val otherRequestOfTheSameBook =
            bookRequestRepository.findAllByBookAndApprovedAndRejectedAndIdNot(
                bookEntity = bookRequestEntity.book,
                approved = false,
                rejected = false,
                id = bookRequestId
            )
        bookRequestRepository.saveAll(otherRequestOfTheSameBook.map { it.copy(rejected = true) })

        bookRequestRepository.save(bookRequestEntity.copy(rejected = false, approved = true))

        return Result.success(true)
    }

    fun rejectBookRequest(
        authentication: Authentication,
        bookRequestId: Long
    ): Result<Boolean> {
        val user = authService.getUser(authentication)
        val bookRequestEntity = bookRequestRepository.findById(bookRequestId).get()
        if (bookRequestEntity.book.user.id != user.id) {
            return Result.failure(Exception("You are not the owner of this book"))
        }

        bookRequestRepository.save(bookRequestEntity.copy(rejected = true))

        return Result.success(true)
    }

}