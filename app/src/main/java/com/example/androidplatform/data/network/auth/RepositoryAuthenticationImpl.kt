package com.example.androidplatform.data.network.auth

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryAuthentication
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.authorization.Authentication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryAuthenticationImpl (
    private val client: NetworkClient,
) : RepositoryAuthentication {

    override suspend fun authenticate(authRequest: AuthRequest): Flow<SearchResultData<Authentication>> = flow {
        val searchResult = client.authenticate(authRequest)
        val data = searchResult.getOrNull()
        val error = searchResult.exceptionOrNull()

        when {
            data != null -> {
                if (data.token.isEmpty()) {
                    emit(SearchResultData.Empty(R.string.auth_error))
                } else {
                    emit(SearchResultData.Data(data.mapToAuthentication()))
                }
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
