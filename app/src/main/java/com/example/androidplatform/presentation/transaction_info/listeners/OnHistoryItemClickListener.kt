package com.example.androidplatform.presentation.transaction_info.listeners

import com.example.androidplatform.domain.models.history.Transaction

fun interface OnHistoryItemClickListener {
    fun onClick(transactionId: Int)
}