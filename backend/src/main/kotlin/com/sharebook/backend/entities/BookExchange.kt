package com.sharebook.backend.entities

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Table
@Entity
data class BookExchangeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_exchange_entity_gen")
    @SequenceGenerator(name = "book_exchange_entity_gen", sequenceName = "book_exchange_entity_seq")
    val id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    val book: BookEntity,
    @ManyToOne
    @JoinColumn(name = "swap_book_id", nullable = true)
    val swapBook: BookEntity?,
    @ManyToOne
    @JoinColumn(name = "book_owner_user_id", nullable = false)
    val bookOwnerUser: UserEntity,
    @ManyToOne
    @JoinColumn(name = "book_renter_user_id", nullable = false)
    val bookRenterUser: UserEntity,
    @CreationTimestamp
    val createdAt: LocalDateTime,
    val dueAt: LocalDateTime,
    @Column(columnDefinition = "boolean default false")
    val returned: Boolean = false,
    val price: Int = 0,
    @OneToOne
    @JoinColumn(name = "book_request_id", nullable = false)
    val bookRequest: BookRequestEntity
)
