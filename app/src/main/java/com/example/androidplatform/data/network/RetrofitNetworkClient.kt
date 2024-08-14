package com.example.androidplatform.data.network

import android.content.Context
import android.content.SharedPreferences
import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.data.network.auth.AuthResponse
import com.example.androidplatform.data.network.change_password.ChangePasswordRequest
import com.example.androidplatform.data.network.launch_operation.LaunchOperationRequest
import com.example.androidplatform.data.network.launch_operation.OperationCode
import com.example.androidplatform.data.network.proceed_operation.ProceedOperationRequestItem
import com.example.androidplatform.data.network.registration.RegistrationRequest
import com.example.androidplatform.data.network.restoration_password.RestoreCodeRequest
import com.example.androidplatform.domain.models.cards.Card
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.domain.models.history.Transaction
import com.example.androidplatform.domain.models.launch_operation.OperationItem
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

    override suspend fun logout(): Result<Void> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val token = sharedPreferences.getString("token", "") ?: ""
                val result = service.logout(token)
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

    override suspend fun restorePassword(
        login: String
    ): Result<Void> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val result = service.restorePassword(RestoreCodeRequest(login))
                Result.success(result)
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

    override suspend fun changePassword(password: String): Result<Void> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val token = sharedPreferences.getString("token", "") ?: ""
                val result = service.changePassword(token, ChangePasswordRequest(password))
                Result.success(result)
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
    
    override suspend fun getHistory(): Result<List<Transaction>> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val token = sharedPreferences.getString("token", "") ?: ""
                val result = service.getHistory(token)
                Result.success(result)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }

    override suspend fun getTransaction(transactionId: Int): Result<Transaction> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val token = sharedPreferences.getString("token", "") ?: ""
                val result = service.getTransaction(token, transactionId)
                Result.success(result)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }
    
    override suspend fun launchOperation(operationCode: OperationCode): Result<OperationItem> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val token = sharedPreferences.getString("token", "") ?: ""
                val result = service.launchOperation(token, LaunchOperationRequest(operationCode.description))
                Result.success(result)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }

    override suspend fun proceedOperation(
        requestId: Int,
        proceedOperationRequestItemList: ArrayList<ProceedOperationRequestItem>
    ): Result<OperationItem> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val token = sharedPreferences.getString("token", "") ?: ""
                val result = service.proceedOperation(
                    token,
                    requestId,
                    proceedOperationRequestItemList)
                Result.success(result)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }

    override suspend fun confirmOperation(
        requestId: Int,
    ): Result<OperationItem> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val token = sharedPreferences.getString("token", "") ?: ""
                val result = service.confirmOperation(
                    token,
                    requestId
                )
                Result.success(result)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: SocketTimeoutException) {
                Result.failure(e)
            }
        }
        return response
    }

    override suspend fun getCards(): Result<List<Card>> {
        if (!isConnected(context)) {
            return Result.failure(ConnectException())
        }
        val response = withContext(Dispatchers.IO) {
            try {
                val token = sharedPreferences.getString("token", "") ?: ""
                val result = service.getCards(token)
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
