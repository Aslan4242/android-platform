package com.example.androidplatform.presentation.registration.models

import androidx.annotation.StringRes

sealed interface RegistrationState {
    object Loading : RegistrationState
    data class Content(val message: String) : RegistrationState
    data class Error(@StringRes val message: Int) : RegistrationState
}
