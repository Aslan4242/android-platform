package com.example.androidplatform.data.network.proceed_operation

import com.google.gson.annotations.SerializedName

data class ProceedOperationRequestItem(
    @SerializedName("identifier")
    val identifier: String,
    @SerializedName("value")
    val value: String
)
