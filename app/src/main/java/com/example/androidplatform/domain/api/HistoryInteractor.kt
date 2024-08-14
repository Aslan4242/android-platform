package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.history.Transaction
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {
    suspend fun getHistory(): Flow<SearchResultData<List<Transaction>>>
}
