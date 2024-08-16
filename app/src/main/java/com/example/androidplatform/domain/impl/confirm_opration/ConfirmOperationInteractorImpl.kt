package com.example.androidplatform.domain.impl.confirm_opration

import com.example.androidplatform.domain.RepositoryConfirmOperation
import com.example.androidplatform.domain.api.ConfirmOperationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow

class ConfirmOperationInteractorImpl(private val repositoryConfirmOperation: RepositoryConfirmOperation) :
    ConfirmOperationInteractor {
    override suspend fun confirmOperation(
        requestId: Int,
    ): Flow<SearchResultData<OperationItem>> {
        return repositoryConfirmOperation.confirmOperation(
            requestId
        )
    }


}
