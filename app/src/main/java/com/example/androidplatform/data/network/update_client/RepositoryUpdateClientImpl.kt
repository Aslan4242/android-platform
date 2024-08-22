package com.example.androidplatform.data.network.update_client

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryUpdateClient
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryUpdateClientImpl(
    private val networkClient: NetworkClient
) : RepositoryUpdateClient {
    override suspend fun updateClient(client: Client): Flow<SearchResultData<Void>> = flow {
        val updateResult = networkClient.updateClient(client)
        val error = updateResult.exceptionOrNull()

        when {
            updateResult.isSuccess -> {
                emit(SearchResultData.Data(Void.TYPE.cast(null)))
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
