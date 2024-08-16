package com.example.androidplatform.presentation.dashboard.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.cards.Card

sealed interface ScreenStateCards {
    object IsLoading : ScreenStateCards
    data class Content(val cards: List<Card>) : ScreenStateCards
    data class Error(@StringRes val message: Int) : ScreenStateCards
    data class Empty(@StringRes val message: Int) : ScreenStateCards
    data class NoInternet(@StringRes val message: Int) : ScreenStateCards
}
