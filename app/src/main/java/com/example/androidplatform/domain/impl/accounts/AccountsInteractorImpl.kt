package com.example.androidplatform.domain.impl.accounts

import com.example.androidplatform.domain.RepositoryAccounts
import com.example.androidplatform.domain.api.AccountsInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Account
import kotlinx.coroutines.flow.Flow

class AccountsInteractorImpl(private val accountsRepository: RepositoryAccounts): AccountsInteractor {
    override suspend fun getAccounts(): Flow<SearchResultData<List<Account>>> {
        return accountsRepository.getAccounts()
    }
}
