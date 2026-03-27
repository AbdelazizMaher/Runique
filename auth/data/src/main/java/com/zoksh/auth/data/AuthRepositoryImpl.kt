package com.zoksh.auth.data

import com.zoksh.auth.domain.AuthRepository
import com.zoksh.core.data.networking.post
import com.zoksh.core.domain.util.DataError
import com.zoksh.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient
): AuthRepository {
    override suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }
}