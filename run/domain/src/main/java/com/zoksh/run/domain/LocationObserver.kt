package com.zoksh.run.domain

import com.zoksh.core.domain.location.LocationWithAltitude
import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    fun observeLocation(interval: Long) : Flow<LocationWithAltitude>
}