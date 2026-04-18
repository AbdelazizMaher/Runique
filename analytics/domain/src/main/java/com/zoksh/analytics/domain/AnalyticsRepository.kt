package com.zoksh.analytics.domain

interface AnalyticsRepository {
    suspend fun getAnalyticsValues(): AnalyticsValues
    suspend fun getAnalyticsHistory(): List<AnalyticsHistoryPoint>
}