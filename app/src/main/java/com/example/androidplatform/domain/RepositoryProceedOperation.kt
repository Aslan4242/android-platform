package com.example.androidplatform.domain

import com.example.androidplatform.data.network.proceed_operation.ProceedOperationRequestItem
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow

interface RepositoryProceedOperation {
    suspend fun proceedOperation(
        requestId: Int,
        proceedOperationRequestItemList: ArrayList<ProceedOperationRequestItem>
    ): Flow<SearchResultData<OperationItem>>
}
