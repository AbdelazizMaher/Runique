package com.zoksh.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.zoksh.core.database.entity.RunEntity

@Dao
interface AnalyticsDao {
    @Query("SELECT SUM(distanceMeters) FROM runentity")
    suspend fun getTotalDistance(): Int

    @Query("SELECT SUM(durationMillis) FROM runentity")
    suspend fun getTotalTimeRun(): Long

    @Query("SELECT MAX(maxSpeedKmh) FROM runentity")
    suspend fun getMaxRunSpeed(): Double

    @Query("SELECT AVG(distanceMeters) FROM runentity")
    suspend fun getAvgDistancePerRun(): Double

    @Query("SELECT AVG((durationMillis / 60000.0) / (distanceMeters / 1000.0)) FROM runentity")
    suspend fun getAvgPacePerRun(): Double

    @Query("SELECT * FROM runentity ORDER BY dateTimeUtc DESC")
    suspend fun getRunsForAnalytics(): List<RunEntity>
}