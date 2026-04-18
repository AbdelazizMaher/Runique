package com.zoksh.analytics.data

import com.zoksh.analytics.domain.AnalyticsHistoryPoint
import com.zoksh.analytics.domain.AnalyticsRepository
import com.zoksh.analytics.domain.AnalyticsValues
import com.zoksh.core.database.dao.AnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.milliseconds

class RoomAnalyticsRepository(
    private val analyticsDao: AnalyticsDao
) : AnalyticsRepository {
    override suspend fun getAnalyticsValues(): AnalyticsValues {
        return withContext(Dispatchers.IO) {
            val totalDistance = async { analyticsDao.getTotalDistance() }
            val totalTimeRun = async { analyticsDao.getTotalTimeRun() }
            val maxRunSpeed = async { analyticsDao.getMaxRunSpeed() }
            val avgDistancePerRun = async { analyticsDao.getAvgDistancePerRun() }
            val avgPacePerRun = async { analyticsDao.getAvgPacePerRun() }

            AnalyticsValues(
                totalDistanceRun = totalDistance.await(),
                totalTimeRun = totalTimeRun.await().milliseconds,
                fastestEverRun = maxRunSpeed.await(),
                avgDistancePerRun = avgDistancePerRun.await(),
                avgPacePerRun = avgPacePerRun.await()
            )
        }
    }

    override suspend fun getAnalyticsHistory(): List<AnalyticsHistoryPoint> {
        return withContext(Dispatchers.IO) {
            analyticsDao.getRunsForAnalytics().map { runEntity ->
                AnalyticsHistoryPoint(
                    dateTimeUtc = ZonedDateTime.parse(runEntity.dateTimeUtc),
                    distanceMeters = runEntity.distanceMeters,
                    paceMinPerKm = if (runEntity.distanceMeters > 0) {
                        (runEntity.durationMillis / 60000.0) / (runEntity.distanceMeters / 1000.0)
                    } else 0.0
                )
            }
        }
    }
}