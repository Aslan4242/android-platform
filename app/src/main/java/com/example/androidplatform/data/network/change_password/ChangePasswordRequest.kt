package com.example.androidplatform.data.network.change_password

import com.google.gson.annotations.SerializedName


data class ChangePasswordRequest(
    @SerializedName("newPassword")
    val password: String
)
