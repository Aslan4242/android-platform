package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow

interface RepositoryChangePassword {
    suspend fun changePassword(password: String): Flow<SearchResultData<Void>>
}