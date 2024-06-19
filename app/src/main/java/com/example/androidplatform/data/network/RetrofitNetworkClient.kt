package com.example.androidplatform.data.network

import android.content.Context
import android.content.SharedPreferences
import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.data.network.auth.AuthResponse
import com.example.androidplatform.data.network.registration.RegistrationRequest
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.util.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RetrofitNetworkClient(
    private val service: ITesterApi,
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : NetworkClient {
    override suspend fun authenticate(authRequest: AuthRequest): Result<AuthResponse> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val result = service.authenticate(request = authRequest)
                sharedPreferences.edit().putString("token", "Bearer ${result.token}").apply()
                Result.success(result)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }

    override suspend fun getClients(): Result<Client> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val token = sharedPreferences.getString("token", "") ?: ""
                val result = service.getClients(token)
                Result.success(result)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }

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
        sex: String
    ): Result<Void> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val result = service.createClient(
                    request = RegistrationRequest(
                        login = login,
                        password = password,
                        phoneNumber = phoneNumber,
                        email = email,
                        firstName = name,
                        lastName = surname,
                        middleName = patronymic,
                        birthdate = birthdate,
                        address = address,
                        sex = sex
                    )
                )
                if (result.isSuccessful) {
                    Result.success(Void.TYPE.cast(null))
                } else {
                    Result.failure(HttpException(result))
                }
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }
}