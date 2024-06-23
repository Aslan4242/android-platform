package com.example.androidplatform.data.network.restoration_password

import com.google.gson.annotations.SerializedName


data class RestoreCodeRequest(
    @SerializedName("login")
    val login: String
)
