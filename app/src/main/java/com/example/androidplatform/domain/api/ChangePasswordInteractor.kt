package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow
interface ChangePasswordInteractor {
    suspend fun changePassword(password: String): Flow<SearchResultData<Void>>
}