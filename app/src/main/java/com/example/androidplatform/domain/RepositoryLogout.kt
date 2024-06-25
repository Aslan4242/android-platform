package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow

interface RepositoryLogout {
    suspend fun logout(): Flow<SearchResultData<Void>>
}