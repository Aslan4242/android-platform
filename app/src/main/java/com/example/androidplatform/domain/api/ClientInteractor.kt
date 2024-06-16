package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Clients
import kotlinx.coroutines.flow.Flow

interface ClientInteractor {
    suspend fun getClients(token: String): Flow<SearchResultData<Clients>>
}
