package com.example.androidplatform.presentation.dashboard.models

import androidx.annotation.StringRes

sealed interface DashboardState {
    data class Content(val authenticationItem: String) : DashboardState
    data class Error(@StringRes val message: Int) : DashboardState
}
