package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow
interface RestorePasswordInteractor {
    suspend fun restorePassword(login: String): Flow<SearchResultData<Void>>
}