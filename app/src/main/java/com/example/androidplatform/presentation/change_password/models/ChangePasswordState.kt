package com.example.androidplatform.presentation.change_password.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.authorization.AuthenticationItem
import com.example.androidplatform.presentation.authentication.models.StateAuthentication

sealed interface ChangePasswordState {
    object Content : ChangePasswordState
    object Loading : ChangePasswordState
    data class ContentAuth(val authenticationItem: AuthenticationItem) : ChangePasswordState
    data class Error(@StringRes val message: Int) : ChangePasswordState
}
