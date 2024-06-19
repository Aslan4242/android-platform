package com.example.androidplatform.data.network.clients

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryClients
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryClientsImpl(
    private val client: NetworkClient,
) : RepositoryClients {
    override suspend fun getClients(): Flow<SearchResultData<Client>> = flow {
        val searchResult = client.getClients()
        val data = searchResult.getOrNull()
        val error = searchResult.exceptionOrNull()

        when {
            data != null -> {
                emit(SearchResultData.Data(data))
            }

            error is ConnectException -> {
                emit(SearchResultData.NoInternet(R.string.no_internet))
            }

            error is SocketTimeoutException -> {
                emit(SearchResultData.NoInternet(R.string.no_internet))
            }

            error is HttpException -> {
                emit(SearchResultData.ErrorServer(R.string.server_error, error.response()?.errorBody()?.string() ?: ""))
            }
        }
    }
}
