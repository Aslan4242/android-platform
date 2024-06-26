package com.example.androidplatform.data.network

import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.data.network.auth.AuthResponse
import com.example.androidplatform.domain.models.clients.Client

interface NetworkClient {
    suspend fun authenticate(authRequest: AuthRequest): Result<AuthResponse>
    suspend fun getClients(): Result<Client>
    suspend fun createClient(
        login: String,
        password: String,
        phoneNumber: String,
        email: String,
        name: String,
        surname: String,
        patronymic: String,
        birthdate: String,
        address: String,
        sex: String
    ): Result<Void>
    suspend fun restorePassword(login: String): Result<Void>
    suspend fun changePassword(password: String): Result<Void>
}
