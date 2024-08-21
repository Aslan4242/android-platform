package com.example.androidplatform.data.network

import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.data.network.auth.AuthResponse
import com.example.androidplatform.data.network.launch_operation.OperationCode
import com.example.androidplatform.data.network.proceed_operation.ProceedOperationRequestItem
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.domain.models.cards.Card
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.domain.models.history.Transaction
import com.example.androidplatform.domain.models.launch_operation.OperationItem

interface NetworkClient {
    suspend fun authenticate(authRequest: AuthRequest): Result<AuthResponse>
    suspend fun getClients(): Result<Client>
    suspend fun logout(): Result<Void>
    suspend fun createClient(
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
    ): Result<Void>
    suspend fun restorePassword(login: String): Result<Void>
    suspend fun changePassword(password: String): Result<Void>
    suspend fun getHistory(): Result<List<Transaction>>
    suspend fun getTransaction(transactionId: Int): Result<Transaction>
    suspend fun launchOperation(operationCode: OperationCode): Result<OperationItem>
    suspend fun proceedOperation(
        requestId: Int,
        proceedOperationRequestItemList: ArrayList<ProceedOperationRequestItem>
    ): Result<OperationItem>
    suspend fun confirmOperation(
        requestId: Int,
    ): Result<OperationItem>
    suspend fun getCards(): Result<List<Card>>
    suspend fun getAccounts(): Result<List<Account>>
}
