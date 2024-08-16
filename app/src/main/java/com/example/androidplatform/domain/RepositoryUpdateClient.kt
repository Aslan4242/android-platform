package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Client
import kotlinx.coroutines.flow.Flow

interface RepositoryUpdateClient {
    suspend fun updateClient(client: Client): Flow<SearchResultData<Void>>
}
