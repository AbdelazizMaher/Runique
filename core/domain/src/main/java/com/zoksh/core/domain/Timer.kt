package com.zoksh.core.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration

object Timer {
    fun timeAndEmit(): Flow<Duration> {
        return flow {
            var lastEmittedTime = System.currentTimeMillis()
            while (true) {
                delay(200L)
                val currentTime = System.currentTimeMillis()
                emit(Duration.ofMillis(currentTime - lastEmittedTime))
                lastEmittedTime = currentTime
            }
        }
    }
}