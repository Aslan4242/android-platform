package com.example.androidplatform.presentation.history.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.history.Transaction

sealed interface HistoryState {
    object IsLoading : HistoryState
    data class Content(val historyList: List<Transaction>) : HistoryState
    data class Error(@StringRes val message: Int) : HistoryState
    data class Empty(@StringRes val message: Int) : HistoryState
    data class NoInternet(@StringRes val message: Int) : HistoryState
}
