package com.example.androidplatform.presentation.account_transfer.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.account.Account

sealed interface AccountTransferState {
    data class Content(val account: Account) : AccountTransferState
    data class Error(@StringRes val message: Int) : AccountTransferState
}
