package com.example.androidplatform.data.network.accounts

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryAccounts
import com.example.androidplatform.domain.RepositoryCards
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.domain.models.cards.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryAccountsImpl(
    private val client: NetworkClient,
) : RepositoryAccounts {
    override suspend fun getAccounts(): Flow<SearchResultData<List<Account>>> = flow {
        val searchResult = client.getAccounts()
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
