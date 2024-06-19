package com.example.androidplatform.data.network.registration

import com.example.androidplatform.R
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.domain.RepositoryRegistration
import com.example.androidplatform.domain.models.SearchResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RepositoryRegistrationImpl(
    private val client: NetworkClient,
) : RepositoryRegistration {
    override suspend fun createClient(
        login: String,
        password: String,
        phoneNumber: String,
        email: String,
        name: String,
        surname: String,
        patronymic: String,
        birthdate: String,
        address: String,
        sex: String,
    ): Flow<SearchResultData<Void>> = flow {
        val searchResult = client.createClient(
            login =login,
            password = password,
            phoneNumber = phoneNumber,
            email = email,
            name = name,
            surname = surname,
            patronymic = patronymic,
            birthdate = birthdate,
            address = address,
            sex = sex
        )
        val error = searchResult.exceptionOrNull()

        when {
            searchResult.isSuccess -> {
                emit(SearchResultData.Data(Void.TYPE.cast(null)))
            }

            error is ConnectException -> {
                emit(SearchResultData.NoInternet(R.string.no_internet))
            }

            error is SocketTimeoutException -> {
                emit(SearchResultData.NoInternet(R.string.no_internet))
            }

            error is HttpException -> {
                emit(SearchResultData.ErrorServer(R.string.server_error))
            }
        }
    }
}
