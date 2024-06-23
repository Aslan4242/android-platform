package com.example.androidplatform.domain.impl.change_password

import com.example.androidplatform.domain.RepositoryChangePassword
import com.example.androidplatform.domain.api.ChangePasswordInteractor
import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow

class ChangePasswordInteractorImpl(private val repositoryChangePassword: RepositoryChangePassword) :
    ChangePasswordInteractor {
    override suspend fun changePassword(password: String): Flow<SearchResultData<Void>> {
        return repositoryChangePassword.changePassword(password)
    }

}
