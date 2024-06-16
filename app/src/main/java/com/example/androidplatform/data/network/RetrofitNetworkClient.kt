package com.example.androidplatform.data.network

import android.content.Context
import com.example.androidplatform.data.dto.ClientDto
import com.example.androidplatform.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RetrofitNetworkClient(private val service: ITesterApi, private val context: Context) : NetworkClient {
    override suspend fun authenticate(authRequest: AuthRequest): Result<AuthResponse> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val result =
                    service.authenticate(request = authRequest)
                Result.success(result)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }

    override suspend fun getClients(token: String): Result<ClientDto> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val result =
                    service.getClients(token)
                Result.success(result)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }
}