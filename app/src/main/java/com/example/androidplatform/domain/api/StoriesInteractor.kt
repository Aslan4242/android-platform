package com.example.androidplatform.domain.api

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.stories.Story
import kotlinx.coroutines.flow.Flow

interface StoriesInteractor {
    suspend fun getStories(): Flow<SearchResultData<List<Story>>>
    suspend fun getStoryById(storyId: Int): Flow<SearchResultData<Story>>
}
