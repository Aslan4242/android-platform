package com.example.androidplatform.presentation.authentication.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.authorization.AuthenticationItem

sealed interface StateAuthentication {
    data class Content(val authenticationItem: AuthenticationItem) : StateAuthentication
    data class Error(@StringRes val message: Int) : StateAuthentication
}
