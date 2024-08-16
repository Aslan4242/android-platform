package com.example.androidplatform.domain.impl.launch_operation

import com.example.androidplatform.data.network.launch_operation.OperationCode
import com.example.androidplatform.domain.RepositoryLaunchOperation
import com.example.androidplatform.domain.api.LaunchOperationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow

class LaunchOperationInteractorImpl(private val repositoryLaunchOperation: RepositoryLaunchOperation) :
    LaunchOperationInteractor {
    override suspend fun launchOperation(operationCode: OperationCode): Flow<SearchResultData<OperationItem>> {
        return repositoryLaunchOperation.launchOperation(operationCode)
    }
}
