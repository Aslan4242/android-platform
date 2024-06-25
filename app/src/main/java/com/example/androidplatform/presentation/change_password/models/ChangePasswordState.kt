package com.example.androidplatform.presentation.change_password.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.authorization.AuthenticationItem

sealed interface ChangePasswordState {
    object Content : ChangePasswordState
    object Loading : ChangePasswordState
    data class ContentAuth(val authenticationItem: AuthenticationItem) : ChangePasswordState
    data class Error(@StringRes val message: Int) : ChangePasswordState
}
