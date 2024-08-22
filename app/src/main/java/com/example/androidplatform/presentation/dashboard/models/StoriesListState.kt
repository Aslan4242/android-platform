package com.example.androidplatform.presentation.dashboard.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.stories.Story

sealed interface StoriesListState {
    object IsLoading : StoriesListState
    data class Content(val stories: List<Story>) : StoriesListState
    data class Error(@StringRes val message: Int) : StoriesListState
    data class Empty(@StringRes val message: Int) : StoriesListState
    data class NoInternet(@StringRes val message: Int) : StoriesListState
}
