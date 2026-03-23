package com.zoksh.core.domain.util

interface DataError : Error {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER_ERROR,
        PAYLOAD_TOO_LARGE,
        SERIALIZATION,
        UNKNOWN,
    }
    enum class Local : DataError {
        DISK_FULL
    }
}