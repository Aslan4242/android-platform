package com.example.androidplatform.domain.impl.restore

import com.example.androidplatform.domain.RepositoryRestorePassword
import com.example.androidplatform.domain.api.RestorePasswordInteractor
import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow

class RestorePasswordInteractorImpl(private val repositoryRegistration: RepositoryRestorePassword) :
    RestorePasswordInteractor {
    override suspend fun restorePassword(login: String): Flow<SearchResultData<Void>> {
        return repositoryRegistration.restorePassword(login = login)
    }

}
