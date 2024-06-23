package com.example.androidplatform.data.network.restoration_password

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryRestorePassword
import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryRestorePasswordImpl(
    private val client: NetworkClient,
) : RepositoryRestorePassword {
    override suspend fun restorePassword(login: String): Flow<SearchResultData<Void>> = flow {
        val searchResult = client.restorePassword(login = login)
        val error = searchResult.exceptionOrNull()

        when {
            searchResult.isSuccess -> {
                emit(SearchResultData.Data(Void.TYPE.cast(null)))
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
