package com.sharebook.backend.entities

import javax.persistence.*

@Entity
@Table
data class ReportEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_entity_gen")
    @SequenceGenerator(name = "report_entity_gen", sequenceName = "report_entity_seq")
    val id: Long = 0,
    @Column(nullable = false)
    val details: String,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
)
