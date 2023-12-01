package com.sharebook.backend.mappers

import com.sharebook.backend.entities.ReportEntity
import com.sharebook.backend.models.Report
import com.sharebook.backend.models.User

fun ReportEntity.toReport(): Report {
    return Report(
        id = id,
        details = details
    )
}

fun Report.toReportEntity(user: User): ReportEntity {
    return ReportEntity(
        id = id,
        details = details,
        user = user.toUserEntity()
    )
}