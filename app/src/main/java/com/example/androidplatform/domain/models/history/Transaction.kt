package com.example.androidplatform.domain.models.history

import android.os.Parcelable
import com.example.androidplatform.domain.models.account.Account

data class Transaction (
    val id: Int,
    val account: Account,
    val receiver: String?,
    val date: String,
    val paymentDate: String,
    val amount: Int,
    val comment: String,
    val reason: String?,
    val state: String,
    val type: String
)
