package com.example.androidplatform.data.network.auth

import com.example.androidplatform.domain.models.authorization.Authentication
import com.example.androidplatform.domain.models.authorization.AuthenticationItem
import com.google.gson.annotations.SerializedName

class AuthResponse(@SerializedName("accessToken") val token: String)

fun AuthResponse.mapToAuthentication(): Authentication {
    return Authentication(
        authenticationItem = AuthenticationItem(this.token)
    )
}
