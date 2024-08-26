package com.example.androidplatform.presentation.card_info.models

import androidx.annotation.StringRes

sealed interface CardCvcState {
    data class Content(val cardCvc: Int) : CardCvcState
    data class Error(@StringRes val message: Int) : CardCvcState
}
