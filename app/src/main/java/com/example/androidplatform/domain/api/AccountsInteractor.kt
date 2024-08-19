package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Account
import kotlinx.coroutines.flow.Flow

interface AccountsInteractor {
    suspend fun getAccounts(): Flow<SearchResultData<List<Account>>>
}
