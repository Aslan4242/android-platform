package com.example.androidplatform.presentation.restoration_password.models

import androidx.annotation.StringRes

sealed interface RestorePasswordState {
    object Content : RestorePasswordState
    object Loading : RestorePasswordState
    data class Error(@StringRes val message: Int) : RestorePasswordState
}
