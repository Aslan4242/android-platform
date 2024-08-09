package com.example.androidplatform.data.network.launch_operation

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryLaunchOperation
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryLaunchOperationImpl(
    private val client: NetworkClient,
) : RepositoryLaunchOperation {
    override suspend fun launchOperation(operationCode: OperationCode): Flow<SearchResultData<OperationItem>> = flow {
        val result = client.launchOperation(operationCode = operationCode)
        val error = result.exceptionOrNull()

        when {
            result.isSuccess -> {
                emit(SearchResultData.Data(result.getOrNull()!!))
            }

            error is ConnectException  -> {
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
