package com.example.androidplatform.presentation.logout.models

import androidx.annotation.StringRes

sealed interface LogoutState {
    data class Content(val message: String) : LogoutState
    data class Error(@StringRes val message: Int) : LogoutState
}
