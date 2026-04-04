package com.zoksh.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bson.types.ObjectId

@Entity
data class RunEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = ObjectId().toHexString(),
    val durationMillis: Long,
    val dateTimeUtc: String,
    val distanceMeters: Int,
    val latitude: Double,
    val longitude: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?
)
