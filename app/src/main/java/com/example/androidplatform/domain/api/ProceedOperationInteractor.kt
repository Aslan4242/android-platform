package com.example.androidplatform.domain.api

import com.example.androidplatform.data.network.proceed_operation.ProceedOperationRequestItem
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow

interface ProceedOperationInteractor {
    suspend fun proceedOperation(
        requestId: Int,
        proceedOperationRequestItemList: ArrayList<ProceedOperationRequestItem>
    ): Flow<SearchResultData<OperationItem>>
}