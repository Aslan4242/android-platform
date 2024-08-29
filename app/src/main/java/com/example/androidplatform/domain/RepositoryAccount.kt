package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Account
import kotlinx.coroutines.flow.Flow

interface RepositoryAccount {
    suspend fun getAccount(accountId: Int): Flow<SearchResultData<Account>>
    suspend fun lockAccountById(accountId: Int): Flow<SearchResultData<Account>>
    suspend fun unlockAccountById(accountId: Int): Flow<SearchResultData<Account>>
}
