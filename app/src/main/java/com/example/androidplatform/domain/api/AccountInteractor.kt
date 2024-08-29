package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Account
import kotlinx.coroutines.flow.Flow

interface AccountInteractor {
    suspend fun getAccount(accountId: Int): Flow<SearchResultData<Account>>
    suspend fun lockAccountById(accountId: Int): Flow<SearchResultData<Account>>
    suspend fun unlockAccountById(accountId: Int): Flow<SearchResultData<Account>>
}
