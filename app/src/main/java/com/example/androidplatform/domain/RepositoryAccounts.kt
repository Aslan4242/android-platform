package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.domain.models.cards.Card
import kotlinx.coroutines.flow.Flow

interface RepositoryAccounts {
    suspend fun getAccounts(): Flow<SearchResultData<List<Account>>>
}
