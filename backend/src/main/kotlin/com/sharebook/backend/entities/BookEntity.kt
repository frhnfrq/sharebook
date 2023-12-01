package com.sharebook.backend.entities

import javax.persistence.*

@Entity
@Table
data class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_entity_gen")
    @SequenceGenerator(name = "book_entity_gen", sequenceName = "book_entity_seq")
    val id: Long = 0,
    @Column(nullable = false)
    val name: String,
    @ElementCollection(fetch = FetchType.LAZY)
    val authors: List<String>,
    @ElementCollection(fetch = FetchType.LAZY)
    val genre: List<String>,
    @Column(nullable = false)
    val coverImage: String,
    @ElementCollection(fetch = FetchType.LAZY)
    val sampleImages: List<String>,
    val available: Boolean = true,
    val swappable: Boolean = true,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
)