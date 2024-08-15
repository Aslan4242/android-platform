package com.example.androidplatform.domain.impl.history

import com.example.androidplatform.domain.RepositoryHistory
import com.example.androidplatform.domain.api.HistoryInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.history.Transaction
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(private val historyRepository: RepositoryHistory) : HistoryInteractor {
    override suspend fun getHistory(): Flow<SearchResultData<List<Transaction>>> {
        return historyRepository.getHistory()
    }

    override suspend fun getTransaction(transactionId: Int): Flow<SearchResultData<Transaction>> {
        return historyRepository.getTransaction(transactionId)
    }
}
