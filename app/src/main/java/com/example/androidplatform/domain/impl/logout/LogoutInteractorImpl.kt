package com.example.androidplatform.domain.impl.logout

import com.example.androidplatform.domain.RepositoryLogout
import com.example.androidplatform.domain.api.LogoutInteractor
import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow

class LogoutInteractorImpl(private val repositoryLogout: RepositoryLogout): LogoutInteractor {
    override suspend fun logout(): Flow<SearchResultData<Void>> {
        return repositoryLogout.logout()
    }


}
