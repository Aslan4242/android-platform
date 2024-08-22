package com.example.androidplatform.presentation.update_user.model

import androidx.annotation.StringRes

interface UpdateState {
    data class Content(val message: String) : UpdateState
    data class Error(@StringRes val message: Int) : UpdateState
}
