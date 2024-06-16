package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Clients
import kotlinx.coroutines.flow.Flow

interface RepositoryClients {
    suspend fun getClients(token: String): Flow<SearchResultData<Clients>>
}