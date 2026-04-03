package com.zoksh.run.presentation.active_run.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.zoksh.core.domain.location.LocationTimestamp

@Composable
fun RuniquePolylines(
    locations: List<List<LocationTimestamp>>,
    modifier: Modifier = Modifier
) {
    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { timeStamp1, timeStamp2 ->
                PolylineUi(
                    location1 = timeStamp1.location.location,
                    location2 = timeStamp2.location.location,
                    color = PolylineColorCalculator.locationsToColor(timeStamp1, timeStamp2)
                )
            }
        }
    }

    polylines.forEach { polyline ->
        polyline.forEach { polylineUi ->
            Polyline(
                points = listOf(
                    LatLng(polylineUi.location1.lat, polylineUi.location1.lng),
                    LatLng(polylineUi.location2.lat, polylineUi.location2.lng)
                ),
                color = polylineUi.color,
                jointType = JointType.BEVEL
            )
        }
    }
}