package com.example.androidplatform.presentation.dashboard.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.domain.models.cards.Card

sealed interface ScreenStateAccounts {
    object IsLoading : ScreenStateAccounts
    data class Content(val accounts: List<Account>) : ScreenStateAccounts
    data class Error(@StringRes val message: Int) : ScreenStateAccounts
    data class Empty(@StringRes val message: Int) : ScreenStateAccounts
    data class NoInternet(@StringRes val message: Int) : ScreenStateAccounts
}
