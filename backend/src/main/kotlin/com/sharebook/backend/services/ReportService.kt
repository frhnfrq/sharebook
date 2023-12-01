package com.sharebook.backend.services

import com.sharebook.backend.exception.CustomException
import com.sharebook.backend.exception.ErrorCode
import com.sharebook.backend.mappers.*
import com.sharebook.backend.models.Report
import com.sharebook.backend.repository.ReportRepository
import com.sharebook.backend.repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val userRepository: UserRepository,
    private val reportRepository: ReportRepository
) {

    fun createReport(authentication: Authentication, report: Report): Result<Report> {
        val userEntity = userRepository.findByEmail(authentication.name)
        return if (userEntity == null) {
            Result.failure(CustomException(errorCode = ErrorCode.USER_NOT_FOUND))
        } else {
            val reportEntity = reportRepository.save(report.toReportEntity(userEntity.toUser()))
            Result.success(reportEntity.toReport())
        }
    }

}