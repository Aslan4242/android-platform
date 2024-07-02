package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow

interface LogoutInteractor {
    suspend fun logout(): Flow<SearchResultData<Void>>
}
