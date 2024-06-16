package com.example.androidplatform.presentation.personal_account.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.clients.ClientsItem

sealed interface ScreenStateClients {
    object IsLoading : ScreenStateClients
    data class Content(val listClients: List<ClientsItem>) : ScreenStateClients
    data class Error(@StringRes val message: Int) : ScreenStateClients
    data class Empty(@StringRes val message: Int) : ScreenStateClients
    data class NoInternet(@StringRes val message: Int) : ScreenStateClients
}
