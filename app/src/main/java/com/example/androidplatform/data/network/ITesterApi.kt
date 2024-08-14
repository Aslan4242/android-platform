package com.example.androidplatform.data.network


import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.data.network.auth.AuthResponse
import com.example.androidplatform.data.network.change_password.ChangePasswordRequest
import com.example.androidplatform.data.network.launch_operation.LaunchOperationRequest
import com.example.androidplatform.data.network.proceed_operation.ProceedOperationRequestItem
import com.example.androidplatform.data.network.registration.RegistrationRequest
import com.example.androidplatform.data.network.restoration_password.RestoreCodeRequest
import com.example.androidplatform.domain.models.cards.Card
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.domain.models.history.Transaction
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ITesterApi {

    @POST("authorization/token")
    suspend fun authenticate(@Body request: AuthRequest): AuthResponse

    @DELETE("authorization/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Void>

    @GET("clients")
    suspend fun getClients(@Header("Authorization") token: String): Client

    @PUT("clients")
    suspend fun createClient(@Body request: RegistrationRequest): Response<Void>

    @PATCH("authorization/restore")
    suspend fun restorePassword(@Body restoreCodeRequest: RestoreCodeRequest): Response<Void>

    @PATCH("clients/password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<Void>

    @GET("transactions")
    suspend fun getHistory(@Header("Authorization") token: String): List<Transaction>

    @PUT("operations")
    suspend fun launchOperation(
        @Header("Authorization") token: String,
        @Body launchOperationRequest: LaunchOperationRequest
    ): OperationItem

    @PATCH("operations")
    suspend fun proceedOperation(
        @Header("Authorization") token: String,
        @Query("requestId") requestId: Int,
        @Body proceedOperationRequestItemList: ArrayList<ProceedOperationRequestItem>
    ): OperationItem

    @POST("operations")
    suspend fun confirmOperation(
        @Header("Authorization") token: String,
        @Query("requestId") requestId: Int
    ): OperationItem

    @GET("cards")
    suspend fun getCards(@Header("Authorization") token: String): List<Card>
}
