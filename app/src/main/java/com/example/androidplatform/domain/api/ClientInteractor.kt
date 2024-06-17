package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Client
import kotlinx.coroutines.flow.Flow

interface ClientInteractor {
    suspend fun getClients(): Flow<SearchResultData<Client>>
}
