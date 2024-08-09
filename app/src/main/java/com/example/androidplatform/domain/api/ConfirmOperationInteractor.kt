package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow

interface ConfirmOperationInteractor {
    suspend fun confirmOperation(
        requestId: Int
    ): Flow<SearchResultData<OperationItem>>
}
