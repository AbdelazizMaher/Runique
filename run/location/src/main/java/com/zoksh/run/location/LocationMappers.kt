package com.zoksh.run.location

import android.location.Location
import com.zoksh.core.domain.location.LocationWithAltitude

fun Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = com.zoksh.core.domain.location.Location(
            lat = latitude,
            lng = longitude
        ),
        altitude = altitude
    )
}