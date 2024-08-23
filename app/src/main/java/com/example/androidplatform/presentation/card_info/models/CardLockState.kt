package com.example.androidplatform.presentation.card_info.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.cards.Card

sealed interface CardLockState {
    data class Content(val card: Card) : CardLockState
    data class Error(@StringRes val message: Int) : CardLockState
}
