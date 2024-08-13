package com.example.androidplatform.domain.models.history

import com.example.androidplatform.domain.models.account.Account

data class Transaction (
    val id: Long,
    val account: Account,
    val receiver: String?,
    val date: String,
    val paymentDate: String,
    val amount: Long,
    val comment: String,
    val reason: String?,
    val state: String,
    val type: String
)