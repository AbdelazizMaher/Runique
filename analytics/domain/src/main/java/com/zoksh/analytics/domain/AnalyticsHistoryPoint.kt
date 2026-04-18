package com.zoksh.analytics.domain

import java.time.ZonedDateTime

data class AnalyticsHistoryPoint(
    val dateTimeUtc: ZonedDateTime,
    val distanceMeters: Int,
    val paceMinPerKm: Double
)
