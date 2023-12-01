package com.sharebook.backend.controllers

import com.sharebook.backend.dto.ResponseDto
import com.sharebook.backend.models.Report
import com.sharebook.backend.services.ReportService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/report")
class ReportController(
    private val reportService: ReportService,
) {

    @PostMapping
    fun report(authentication: Authentication, report: Report): ResponseDto<Report> {
        val result = reportService.createReport(authentication, report)
        return ResponseDto(result)
    }

}