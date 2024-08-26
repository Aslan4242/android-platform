package com.example.androidplatform.presentation.pin_code.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.cards.Card

sealed interface ActivationState {
    data class Content(val card: Card) : ActivationState
    data class Error(@StringRes val message: Int) : ActivationState
}
