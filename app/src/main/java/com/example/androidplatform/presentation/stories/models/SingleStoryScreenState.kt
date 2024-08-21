package com.example.androidplatform.presentation.stories.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.stories.Story

interface SingleStoryScreenState {
    object IsLoading : SingleStoryScreenState
    data class Content(val story: Story) : SingleStoryScreenState
    data class Error(@StringRes val message: Int) : SingleStoryScreenState
    data class Empty(@StringRes val message: Int) : SingleStoryScreenState
    data class NoInternet(@StringRes val message: Int) : SingleStoryScreenState
}
