package com.example.androidplatform.domain.api

import com.example.androidplatform.data.network.launch_operation.OperationCode
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow

interface LaunchOperationInteractor {
    suspend fun launchOperation(operationCode: OperationCode): Flow<SearchResultData<OperationItem>>
}
