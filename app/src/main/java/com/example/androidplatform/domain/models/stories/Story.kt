package com.example.androidplatform.domain.models.stories

import androidx.annotation.DrawableRes

data class Story(
    val id: Int,
    val label: String,
    val text: String,
    @DrawableRes val image: Int
)
