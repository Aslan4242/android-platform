package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.history.Transaction
import kotlinx.coroutines.flow.Flow

interface RepositoryHistory {
    suspend fun getHistory(): Flow<SearchResultData<List<Transaction>>>
}