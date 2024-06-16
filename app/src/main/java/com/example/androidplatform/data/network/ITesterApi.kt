package com.example.androidplatform.data.network


import com.example.androidplatform.data.dto.ClientDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ITesterApi {

    @POST("authorization/token")
    suspend fun authenticate(@Body request: AuthRequest): AuthResponse

    @GET("clients")
    suspend fun getClients(@Header("Authorization") token: String): ClientDto


}