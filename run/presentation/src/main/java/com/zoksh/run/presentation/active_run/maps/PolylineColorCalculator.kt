package com.zoksh.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.compose.ui.graphics.toArgb
import com.zoksh.core.domain.location.LocationTimestamp
import kotlin.math.abs

object PolylineColorCalculator {

    fun locationsToColor(location1: LocationTimestamp, location2: LocationTimestamp): Color {
        val distanceMeters = location1.location.location.distanceTo(location2.location.location)
        val timeDifference =
            abs((location2.durationTimestamp - location1.durationTimestamp).inWholeSeconds)
        
        val speedKm = if (timeDifference > 0) {
            (distanceMeters / timeDifference) * 3.6
        } else 0.0

        return interpolateColor(
            speedKm = speedKm,
            minSpeedKm = 5.0,
            maxSpeedKm = 20.0,
            colorStart = Color.Green,
            colorMid = Color.Yellow,
            colorEnd = Color.Red
        )
    }

    private fun interpolateColor(
        speedKm: Double,
        minSpeedKm: Double,
        maxSpeedKm: Double,
        colorStart: Color,
        colorMid: Color,
        colorEnd: Color
    ): Color {
        val ratio = ((speedKm - minSpeedKm) / (maxSpeedKm - minSpeedKm)).coerceIn(0.0..1.0)
        val colorInt = if (ratio <= 0.5) {
            val startToMidRatio = ratio / 0.5
            ColorUtils.blendARGB(
                colorStart.toArgb(), colorMid.toArgb(), startToMidRatio.toFloat()
            )
        } else {
            val midToEndRatio = (ratio - 0.5) / 0.5
            ColorUtils.blendARGB(
                colorMid.toArgb(), colorEnd.toArgb(), midToEndRatio.toFloat()
            )
        }
        return Color(colorInt)
    }
}