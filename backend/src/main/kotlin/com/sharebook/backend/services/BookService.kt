package com.sharebook.backend.services

import com.sharebook.backend.dto.CreateBookRequestDto
import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.entities.BookExchangeEntity
import com.sharebook.backend.entities.BookRequestEntity
import com.sharebook.backend.mappers.*
import com.sharebook.backend.models.Book
import com.sharebook.backend.models.BookExchange
import com.sharebook.backend.models.BookRequest
import com.sharebook.backend.repository.BookExchangeRepository
import com.sharebook.backend.repository.BookRepository
import com.sharebook.backend.repository.BookRequestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BookService(
    private val authService: AuthService,
    private val bookRepository: BookRepository,
    private val bookRequestRepository: BookRequestRepository,
    private val bookExchangeRepository: BookExchangeRepository,
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

    fun getSuggestedBooks(authentication: Authentication): Result<List<Book>> {
        val user = authService.getUser(authentication)
        val books: MutableList<Book> = mutableListOf()

        books.addAll(bookRepository.findAllByUserIsNot(user.toUserEntity()).map(BookEntity::toBook))

        return Result.success((books))
    }

    fun createBook(authentication: Authentication, book: Book): Result<Book> {
        val user = authService.getUser(authentication)
        val createdBookEntity = bookRepository.save(book.toBookEntity(user, onRent = false))
        return Result.success(createdBookEntity.toBook())
    }

    fun editBook(authentication: Authentication, book: Book): Result<Book> {
        val user = authService.getUser(authentication)
        val existingBook = bookRepository.findById(book.id)
        if (existingBook.isPresent) {
            val createdBookEntity = bookRepository.save(book.toBookEntity(user, onRent = existingBook.get().onRent))
            return Result.success(createdBookEntity.toBook())
        } else {
            return Result.failure(Exception("Invalid book received"))
        }
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
        var swapBookEntity: BookEntity? = null

        if (createBookRequestDto.swapBookId != null) {
            swapBookEntity = bookRepository.findByIdOrNull(createBookRequestDto.swapBookId)
        }

        val previousRequest = bookRequestRepository.findByUserAndBookAndApprovedAndRejected(
            user.toUserEntity(),
            bookEntity,
            approved = false,
            rejected = false
        )

        if (previousRequest != null) {
            return Result.failure(Exception("You have already requested for this book"))
        }

        if (bookEntity.onRent) {
            return Result.failure(Exception("This book is already on rent"))
        }

        if (swapBookEntity?.onRent == true) {
            return Result.failure(Exception("The book you chose to swap with is already on rent"))
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
        val bookRequestEntity = bookRequestRepository.save(
            bookRequest.toBookRequestEntity(
                user,
                bookOnRent = false,
                swapBookOnRent = false
            )
        )

        return Result.success(bookRequestEntity.toBookRequest())
    }

    fun approveBookRequest(
        authentication: Authentication,
        bookRequestId: Long,
    ): Result<BookExchange> {
        val user = authService.getUser(authentication)
        val bookRequestEntity = bookRequestRepository.findById(bookRequestId).get()
        if (bookRequestEntity.book.user.id != user.id) {
            return Result.failure(Exception("You are not the owner of this book"))
        }

        val otherRequestOfTheSameBook =
            bookRequestRepository.findAllByBookAndApprovedAndRejectedAndIdIsNot(
                bookEntity = bookRequestEntity.book,
                approved = false,
                rejected = false,
                id = bookRequestId
            )
        bookRequestRepository.saveAll(otherRequestOfTheSameBook.map { it.copy(rejected = true) })

        bookRequestRepository.save(bookRequestEntity.copy(rejected = false, approved = true))


        val bookExchangeEntity = bookExchangeRepository.save(
            BookExchangeEntity(
                book = bookRequestEntity.book,
                swapBook = bookRequestEntity.swapBook,
                bookOwnerUser = user.toUserEntity(),
                bookRenterUser = bookRequestEntity.user,
                createdAt = LocalDateTime.now(),
                dueAt = LocalDateTime.now().plusDays(7),
                returned = false,
                price = bookRequestEntity.book.price,
                bookRequest = bookRequestEntity
            ),
        )

        bookRepository.save(bookRequestEntity.book.copy(onRent = true))

        return Result.success(bookExchangeEntity.toBookExchange())
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


    fun returnBook(
        authentication: Authentication,
        bookExchangeId: Long
    ): Result<BookExchange> {
        val bookExchangeEntity = bookExchangeRepository.findById(bookExchangeId)
        if (bookExchangeEntity.isPresent) {
            val updatedBookExchangeEntity = bookExchangeRepository.save(bookExchangeEntity.get().copy(returned = true))
            bookRepository.save(bookExchangeEntity.get().book.copy(onRent = false))
            if (bookExchangeEntity.get().swapBook != null) {
                bookRepository.save(bookExchangeEntity.get().swapBook!!.copy(onRent = false))
            }
            return Result.success(updatedBookExchangeEntity.toBookExchange())
        } else {
            return Result.failure(Exception("Invalid book exchange ID"))
        }
    }

    fun getBook(authentication: Authentication, id: Long): Result<Book> {
        val bookEntity = bookRepository.findById(id)
        if (bookEntity.isPresent) {
            return Result.success(bookEntity.get().toBook())
        } else {
            return Result.failure(Exception("Invalid book ID"))
        }
    }

    fun getBookRequestsForBookId(authentication: Authentication, bookId: Long): Result<List<BookRequest>> {
        val bookEntity = bookRepository.findById(bookId).get()
        val requests = bookRequestRepository.findAllByBook(bookEntity)
        return Result.success(requests.map(BookRequestEntity::toBookRequest))
    }

    fun getOwnerBookExchanges(authentication: Authentication): Result<List<BookExchange>> {
        val exchanges =
            bookExchangeRepository.findAllByBookOwnerUser(authService.getUser(authentication).toUserEntity())

        return Result.success(exchanges.map(BookExchangeEntity::toBookExchange))
    }

    fun getRenterBookExchanges(authentication: Authentication): Result<List<BookExchange>> {
        val exchanges =
            bookExchangeRepository.findAllByBookRenterUser(authService.getUser(authentication).toUserEntity())

        return Result.success(exchanges.map(BookExchangeEntity::toBookExchange))
    }
}