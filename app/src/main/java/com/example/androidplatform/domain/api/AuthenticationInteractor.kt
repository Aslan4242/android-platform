package com.example.androidplatform.domain.api

import com.example.androidplatform.data.network.AuthRequest
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.authorization.Authentication
import kotlinx.coroutines.flow.Flow

interface AuthenticationInteractor {
    suspend fun authentication(request: AuthRequest): Flow<SearchResultData<Authentication>>
}
