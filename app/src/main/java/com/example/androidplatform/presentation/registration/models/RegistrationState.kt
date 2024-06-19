package com.example.androidplatform.presentation.registration.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.authorization.AuthenticationItem

sealed interface RegistrationState {
    data class Content(val message: String) : RegistrationState
    data class Error(@StringRes val message: Int) : RegistrationState
}
