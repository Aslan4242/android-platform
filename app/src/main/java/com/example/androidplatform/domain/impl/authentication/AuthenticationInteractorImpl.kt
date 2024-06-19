package com.example.androidplatform.domain.impl.authentication

import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.domain.RepositoryAuthentication
import com.example.androidplatform.domain.api.AuthenticationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.authorization.Authentication
import kotlinx.coroutines.flow.Flow

class AuthenticationInteractorImpl(private val authenticationRepository: RepositoryAuthentication): AuthenticationInteractor {
    override suspend fun authentication(request: AuthRequest): Flow<SearchResultData<Authentication>> {
        return authenticationRepository.authenticate(request)
    }
}
