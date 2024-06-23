package com.example.androidplatform.presentation.restoration_password.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.authorization.AuthenticationItem
import com.example.androidplatform.presentation.authentication.models.StateAuthentication

sealed interface RestorePasswordState {
    object Content : RestorePasswordState
    object Loading : RestorePasswordState
    data class Error(@StringRes val message: Int) : RestorePasswordState
}
