package com.example.androidplatform.presentation.stories.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.stories.Story

interface StoriesScreenState {
    object IsLoading : StoriesScreenState
    data class Content(val story: Story) : StoriesScreenState
    data class Error(@StringRes val message: Int) : StoriesScreenState
    data class Empty(@StringRes val message: Int) : StoriesScreenState
    data class NoInternet(@StringRes val message: Int) : StoriesScreenState
}
