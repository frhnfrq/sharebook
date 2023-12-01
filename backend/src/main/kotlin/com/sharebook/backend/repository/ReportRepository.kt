package com.sharebook.backend.repository

import com.sharebook.backend.entities.ReportEntity
import com.sharebook.backend.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReportRepository : JpaRepository<ReportEntity, Long> {

    @Query(
        """
        SELECT r FROM ReportEntity r
    """
    )
    fun getAllReport(): List<ReportEntity>
    fun findAllByUser(userEntity: UserEntity): List<ReportEntity>
}