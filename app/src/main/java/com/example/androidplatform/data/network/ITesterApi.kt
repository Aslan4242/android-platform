package com.example.androidplatform.data.network


import com.example.androidplatform.data.network.auth.AuthRequest
import com.example.androidplatform.data.network.auth.AuthResponse
import com.example.androidplatform.data.network.change_password.ChangePasswordRequest
import com.example.androidplatform.data.network.registration.RegistrationRequest
import com.example.androidplatform.data.network.restoration_password.RestoreCodeRequest
import com.example.androidplatform.domain.models.clients.Client
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

interface ITesterApi {

    @POST("authorization/token")
    suspend fun authenticate(@Body request: AuthRequest): AuthResponse

    @DELETE("authorization/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Void>

    @GET("clients")
    suspend fun getClients(@Header("Authorization") token: String): Client

    @PUT("clients")
    suspend fun createClient(@Body request: RegistrationRequest): Response<Void>

    @PATCH("clients")
    suspend fun updateClient(
        @Header("Authorization") token: String,
        @Body request: Client
    ): Response<Void>

    @PATCH("authorization/restore")
    suspend fun restorePassword(@Body restoreCodeRequest: RestoreCodeRequest): Response<Void>

    @PATCH("clients/password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<Void>
}