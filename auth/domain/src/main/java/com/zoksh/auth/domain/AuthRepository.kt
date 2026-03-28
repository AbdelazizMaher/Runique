package com.zoksh.auth.domain

import com.zoksh.core.domain.util.DataError
import com.zoksh.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
}