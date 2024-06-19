package com.example.androidplatform.domain

import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.authorization.Authentication
import kotlinx.coroutines.flow.Flow

interface RepositoryAuthentication {
    suspend fun authenticate(authRequest: AuthRequest): Flow<SearchResultData<Authentication>>
}