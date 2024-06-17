package com.example.androidplatform.presentation.personal_account.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.clients.Client

sealed interface ScreenStateClients {
    object IsLoading : ScreenStateClients
    data class Content(val client: Client) : ScreenStateClients
    data class Error(@StringRes val message: Int) : ScreenStateClients
    data class Empty(@StringRes val message: Int) : ScreenStateClients
    data class NoInternet(@StringRes val message: Int) : ScreenStateClients
}
