package com.example.androidplatform.domain.impl.account

import com.example.androidplatform.domain.RepositoryAccount
import com.example.androidplatform.domain.api.AccountInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Account
import kotlinx.coroutines.flow.Flow

class AccountInteractorImpl(private val accountRepository: RepositoryAccount): AccountInteractor {
    override suspend fun getAccount(accountId: Int): Flow<SearchResultData<Account>> {
        return accountRepository.getAccount(accountId)
    }

    override suspend fun lockAccountById(accountId: Int): Flow<SearchResultData<Account>> {
        return accountRepository.lockAccountById(accountId)
    }

    override suspend fun unlockAccountById(accountId: Int): Flow<SearchResultData<Account>> {
        return accountRepository.unlockAccountById(accountId)
    }
}
