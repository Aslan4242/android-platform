package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow

interface RepositoryConfirmOperation {
    suspend fun confirmOperation(
        requestId: Int,
    ): Flow<SearchResultData<OperationItem>>
}