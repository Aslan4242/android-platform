package com.example.androidplatform.data.network

import com.example.androidplatform.data.dto.ClientDto

interface NetworkClient {
    suspend fun authenticate(authRequest: AuthRequest): Result<AuthResponse>
    suspend fun getClients(token: String): Result<ClientDto>
}
