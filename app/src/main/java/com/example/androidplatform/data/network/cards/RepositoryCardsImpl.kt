package com.example.androidplatform.data.network.cards

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryCards
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.cards.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryCardsImpl(
    private val client: NetworkClient,
) : RepositoryCards {
    override suspend fun getCards(): Flow<SearchResultData<List<Card>>> = flow {
        val searchResult = client.getCards()
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

    override suspend fun getCardCvc(cardId: Int): Flow<SearchResultData<Int>> = flow {
        val searchResult = client.getCardCvc(cardId)
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

    override suspend fun lockCardById(cardId: Int): Flow<SearchResultData<Card>> = flow {
        val searchResult = client.lockCardById(cardId)
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

    override suspend fun unlockCardById(cardId: Int): Flow<SearchResultData<Card>> = flow {
        val searchResult = client.unlockCardById(cardId)
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
