package com.sharebook.backend.entities

import javax.persistence.*
@Entity
@Table
data class WishEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wishlist_entity_gen")
    @SequenceGenerator(name = "wishlist_entity_gen", sequenceName = "wishlist_entity_seq")
    val id: Long = 0,
    @Column(nullable = false)
    val name: String,
    @ElementCollection(fetch = FetchType.LAZY)
    val authors: List<String>,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
)
