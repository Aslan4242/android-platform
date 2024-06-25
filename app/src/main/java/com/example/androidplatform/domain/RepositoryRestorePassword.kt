package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow

interface RepositoryRestorePassword {
    suspend fun restorePassword(login: String): Flow<SearchResultData<Void>>
}