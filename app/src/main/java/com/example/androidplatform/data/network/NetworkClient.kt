package com.example.androidplatform.data.network

import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.data.network.auth.AuthResponse
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.domain.models.history.Transaction

interface NetworkClient {
    suspend fun authenticate(authRequest: AuthRequest): Result<AuthResponse>
    suspend fun getClients(): Result<Client>
    suspend fun logout(): Result<Void>
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
    suspend fun getHistory(): Result<List<Transaction>>
}
