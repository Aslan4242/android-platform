package com.example.androidplatform.data.network.confirm_operation

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryConfirmOperation
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryConfirmOperationImpl(
    private val client: NetworkClient,
) : RepositoryConfirmOperation {
    override suspend fun confirmOperation(
        requestId: Int
    ): Flow<SearchResultData<OperationItem>> = flow {
        val result = client.confirmOperation(
            requestId = requestId
        )
        val error = result.exceptionOrNull()

        when {
            result.isSuccess -> {
                emit(SearchResultData.Data(result.getOrNull()!!))
            }

            error is ConnectException -> {
                emit(SearchResultData.NoInternet(R.string.no_internet))
            }

            error is SocketTimeoutException -> {
                emit(SearchResultData.NoInternet(R.string.no_internet))
            }

            error is HttpException -> {
                emit(
                    SearchResultData.ErrorServer(
                        R.string.server_error,
                        error.response()?.errorBody()?.string() ?: ""
                    )
                )
            }
        }
    }
}
