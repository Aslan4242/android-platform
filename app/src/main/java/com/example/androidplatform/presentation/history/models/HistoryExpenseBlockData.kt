package com.example.androidplatform.presentation.history.models

import androidx.annotation.StringRes

data class HistoryExpenseBlockData (
    @StringRes val header: Int,
    val text: String
)