package com.example.androidplatform.domain.impl.proceed_operation

import com.example.androidplatform.data.network.proceed_operation.ProceedOperationRequestItem
import com.example.androidplatform.domain.RepositoryProceedOperation
import com.example.androidplatform.domain.api.ProceedOperationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow

class ProceedOperationInteractorImpl(private val repositoryProceedOperation: RepositoryProceedOperation) :
    ProceedOperationInteractor {
    override suspend fun proceedOperation(
        requestId: Int,
        proceedOperationRequestItemList: ArrayList<ProceedOperationRequestItem>
    ): Flow<SearchResultData<OperationItem>> {
        return repositoryProceedOperation.proceedOperation(
            requestId,
            proceedOperationRequestItemList
        )
    }
}
