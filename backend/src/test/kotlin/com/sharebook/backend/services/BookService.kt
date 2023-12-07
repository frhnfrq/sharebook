package com.sharebook.backend.services

import com.sharebook.backend.entities.BookEntity
import com.sharebook.backend.entities.UserEntity
import com.sharebook.backend.mappers.toBookEntity
import com.sharebook.backend.mappers.toSafeUser
import com.sharebook.backend.mappers.toUser
import com.sharebook.backend.mappers.toUserEntity
import com.sharebook.backend.models.Book
import com.sharebook.backend.models.User
import com.sharebook.backend.repository.BookExchangeRepository
import com.sharebook.backend.repository.BookRepository
import com.sharebook.backend.repository.BookRequestRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@ExtendWith(MockitoExtension::class)
class BookServiceTest {

    @Mock
    private lateinit var authService: AuthService

    @Mock
    private lateinit var bookRepository: BookRepository

    @Mock
    private lateinit var bookRequestRepository: BookRequestRepository

    @Mock
    private lateinit var bookExchangeRepository: BookExchangeRepository

    @InjectMocks
    private lateinit var bookService: BookService

    private lateinit var mockAuthentication: Authentication

    @BeforeEach
    fun setUp() {
        // You can initialize your mock authentication here
        mockAuthentication = createMockAuthentication()
    }

    @Test
    fun `createBook should return a created book`() {
        // Arrange
        val newBook = createMockBook()
        val expectedCreatedBook = createMockBook()

        // Mocking behavior using whenever
        whenever(authService.getUser(mockAuthentication)).thenReturn(createMockUser().toUser())
        whenever(bookRepository.save(newBook.toBookEntity(createMockUser().toUser(), onRent = false))).thenReturn(
            createMockBookEntity()
        )

        // Act
        val result = bookService.createBook(mockAuthentication, newBook)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(expectedCreatedBook, result.getOrNull())
    }

    @Test
    fun `getBooks should return a list of books`() {
        // Arrange
        val user = createMockUser().toUser()
        val expectedBooks = listOf(createMockBook(), createMockBook())

        // Mocking behavior using whenever
        whenever(authService.getUser(mockAuthentication)).thenReturn(user)
        whenever(bookRepository.findByUser(user.toUserEntity())).thenReturn(expectedBooks.map {
            it.toBookEntity(
                user,
                onRent = false
            )
        })

        // Act
        val result = bookService.getBooks(mockAuthentication, null)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(expectedBooks, result.getOrNull())
    }

    @Test
    fun `editBook should return an edited book`() {
        // Arrange
        val user = createMockUser().toUser()
        val editedBook = createMockBook().copy(name = "Old Book")
        val expectedEditedBook = editedBook.copy(name = "New Book", user = createMockUser().toUser().toSafeUser())

        // Mocking behavior using whenever
        whenever(authService.getUser(mockAuthentication)).thenReturn(user)
        whenever(bookRepository.findById(editedBook.id)).thenReturn(Optional.of(createMockBookEntity()))
        whenever(
            bookRepository.save(
                editedBook.toBookEntity(
                    user,
                    onRent = false
                )
            )
        ).thenReturn(expectedEditedBook.toBookEntity(user, onRent = false))

        // Act
        val result = bookService.editBook(mockAuthentication, editedBook)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(expectedEditedBook.name, result.getOrNull()?.name)
    }


    private fun createMockBook(): Book {
        return Book(
            id = 1,
            name = "Sample Book",
            authors = listOf("Author 1", "Author 2"),
            genre = listOf("Genre 1", "Genre 2"),
            coverImage = "cover.jpg",
            sampleImages = listOf("sample1.jpg", "sample2.jpg"),
            available = true,
            swappable = true,
            price = 10,
            user = createMockUser().toUser().toSafeUser()
        )
    }

    private fun createMockAuthentication(): Authentication {
        // Mocking authentication
        val userDetails = object : UserDetails {
            override fun getAuthorities() = emptySet<GrantedAuthority>()

            override fun getPassword() = ""

            override fun getUsername() = ""

            override fun isAccountNonExpired() = true

            override fun isAccountNonLocked() = true

            override fun isCredentialsNonExpired() = true

            override fun isEnabled() = true
        }

        return UsernamePasswordAuthenticationToken(userDetails, null, emptySet())
    }

    private fun createMockBookEntity(): BookEntity {
        return BookEntity(
            id = 1,
            name = "Sample Book",
            authors = listOf("Author 1", "Author 2"),
            genre = listOf("Genre 1", "Genre 2"),
            coverImage = "cover.jpg",
            sampleImages = listOf("sample1.jpg", "sample2.jpg"),
            available = true,
            swappable = true,
            price = 10,
            user = createMockUser(),
            onRent = false
        )
    }

    private fun createMockUser(): UserEntity {
        return UserEntity(
            id = 1,
            name = "John Doe",
            email = "john.doe@example.com",
            password = "password",
            address = "123 Main St",
            profilePicture = "profile.jpg"
        )
    }


}