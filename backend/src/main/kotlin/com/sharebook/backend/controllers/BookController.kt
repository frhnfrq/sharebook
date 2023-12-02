package com.sharebook.backend.controllers

import com.sharebook.backend.dto.CreateBookRequestDto
import com.sharebook.backend.dto.ResponseDto
import com.sharebook.backend.models.Book
import com.sharebook.backend.models.BookExchange
import com.sharebook.backend.models.BookRequest
import com.sharebook.backend.services.BookService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api/books")
class BookController(
    private val bookService: BookService
) {

    @GetMapping
    fun getBooks(authentication: Authentication): ResponseDto<List<Book>> {
        val booksResult = bookService.getBooks(authentication, null)
        return ResponseDto(booksResult)
    }

    @GetMapping("{id}")
    fun getBook(authentication: Authentication, @PathVariable("id") id: Long): ResponseDto<Book> {
        val bookResult = bookService.getBook(authentication, id)
        return ResponseDto(bookResult)
    }

    @GetMapping("suggestion")
    fun getSuggestedBooks(authentication: Authentication): ResponseDto<List<Book>> {
        val booksResult = bookService.getSuggestedBooks(authentication)
        return ResponseDto(booksResult)
    }

    @GetMapping("search")
    fun searchBooks(authentication: Authentication, @RequestParam query: String): ResponseDto<List<Book>> {
        val booksResult = bookService.getBooks(authentication, query)
        return ResponseDto(booksResult)
    }

    @PostMapping
    fun createBook(authentication: Authentication, @RequestBody book: Book): ResponseDto<Book> {
        val bookResult = bookService.createBook(authentication, book)
        return ResponseDto(bookResult)
    }

    @PutMapping
    fun editBook(authentication: Authentication, @RequestBody book: Book): ResponseDto<Book> {
        val bookResult = bookService.editBook(authentication, book)
        return ResponseDto(bookResult)
    }

    @DeleteMapping("{id}")
    fun deleteBook(authentication: Authentication, @PathVariable("id") id: Long): ResponseDto<Boolean> {
        val bookResult = bookService.deleteBook(authentication, id)
        return ResponseDto(bookResult)
    }

    @PostMapping("request")
    fun requestBook(
        authentication: Authentication,
        @RequestBody createBookRequestDto: CreateBookRequestDto
    ): ResponseDto<BookRequest> {
        val result = bookService.createBookRequest(authentication, createBookRequestDto)
        return ResponseDto(result)
    }

    @PostMapping("request/approve/{id}")
    fun approveRequest(authentication: Authentication, @PathVariable("id") id: Long): ResponseDto<BookExchange> {
        val result = bookService.approveBookRequest(authentication, bookRequestId = id)
        return ResponseDto(result)
    }

    @PostMapping("request/reject/{id}")
    fun rejectRequest(authentication: Authentication, @PathVariable("id") id: Long): ResponseDto<Boolean> {
        val result = bookService.rejectBookRequest(authentication, bookRequestId = id)
        return ResponseDto(result)
    }

}