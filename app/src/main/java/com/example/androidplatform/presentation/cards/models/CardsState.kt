package com.example.androidplatform.presentation.cards.models

import androidx.annotation.StringRes

sealed interface CardsState {
    data class Content(val authenticationItem: String) : CardsState
    data class Error(@StringRes val message: Int) : CardsState
}
