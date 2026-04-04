package com.zoksh.run.network

import com.zoksh.core.domain.location.Location
import com.zoksh.core.domain.run.Run
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunDto.toRun(): Run {
    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = Instant.parse(dateTimeUtc).atZone(ZoneId.of("UTC")),
        distanceMeters = distanceMeters,
        location = Location(
            lat = lat,
            lng = long
        ),
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )
}

fun Run.toCreateRunRequest(): CreateRunRequest {
    return CreateRunRequest(
        durationMillis = duration.inWholeMilliseconds,
        distanceMeters = distanceMeters,
        epochMillis = dateTimeUtc.toInstant().toEpochMilli() * 1000L,
        lat = location.lat,
        long = location.lng,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        id = id!!
    )
}