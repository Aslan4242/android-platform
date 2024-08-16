package com.example.androidplatform.data.network.launch_operation

import com.google.gson.annotations.SerializedName

data class LaunchOperationRequest(
    @SerializedName("operationCode")
    val operationCode: String
)
