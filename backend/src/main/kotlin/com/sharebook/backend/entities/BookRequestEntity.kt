package com.sharebook.backend.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class BookRequestEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_book_entity_gen")
    @SequenceGenerator(name = "request_book_entity_gen", sequenceName = "request_book_entity_seq")
    val id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    val book: BookEntity,
    @ManyToOne
    @JoinColumn(name = "swap_book_id", nullable = true)
    val swapBook: BookEntity?,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
    @Column(updatable = false)
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,
    @Column(columnDefinition = "boolean default false")
    val approved: Boolean = false,
    @Column(columnDefinition = "boolean default false")
    val rejected: Boolean = false,
)
