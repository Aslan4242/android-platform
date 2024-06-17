package com.example.androidplatform.data.network

import com.example.androidplatform.domain.models.clients.Client

interface NetworkClient {
    suspend fun authenticate(authRequest: AuthRequest): Result<AuthResponse>
    suspend fun getClients(): Result<Client>
}
