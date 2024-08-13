package com.example.androidplatform.data.network.history

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryHistory
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.history.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryHistoryImpl(
    private val client: NetworkClient
) : RepositoryHistory {
    override suspend fun getHistory(): Flow<SearchResultData<List<Transaction>>> = flow {
        val historyResult = client.getHistory()
        val data = historyResult.getOrNull()
        val error = historyResult.exceptionOrNull()

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