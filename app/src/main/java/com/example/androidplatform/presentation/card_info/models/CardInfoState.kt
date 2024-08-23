package com.example.androidplatform.presentation.card_info.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.cards.Card

sealed interface CardInfoState {
    data class Content(val card: Card) : CardInfoState
    data class Error(@StringRes val message: Int) : CardInfoState
}
