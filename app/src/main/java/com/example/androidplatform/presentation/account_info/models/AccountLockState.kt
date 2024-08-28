package com.example.androidplatform.presentation.account_info.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.account.Account

sealed interface AccountLockState {
    data class Content(val account: Account) : AccountLockState
    data class Error(@StringRes val message: Int) : AccountLockState
}
