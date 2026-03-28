package com.zoksh.auth.data

import com.zoksh.auth.domain.AuthRepository
import com.zoksh.core.data.networking.post
import com.zoksh.core.domain.AuthInfo
import com.zoksh.core.domain.SessionStorage
import com.zoksh.core.domain.util.DataError
import com.zoksh.core.domain.util.EmptyResult
import com.zoksh.core.domain.util.Result
import com.zoksh.core.domain.util.asEmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
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

    override suspend fun login(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        val response =  httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )

        if (response is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = response.data.accessToken,
                    refreshToken = response.data.refreshToken,
                    userId = response.data.userId
                )
            )
        }
        return response.asEmptyResult()
    }
}