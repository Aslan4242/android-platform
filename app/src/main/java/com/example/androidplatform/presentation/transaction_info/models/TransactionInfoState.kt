package com.example.androidplatform.presentation.transaction_info.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.history.Transaction

sealed interface TransactionInfoState {
    object IsLoading : TransactionInfoState
    data class Content(val transaction: Transaction) : TransactionInfoState
    data class Error(@StringRes val message: Int) : TransactionInfoState
    data class Empty(@StringRes val message: Int) : TransactionInfoState
    data class NoInternet(@StringRes val message: Int) : TransactionInfoState
}