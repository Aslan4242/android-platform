package com.example.androidplatform.domain

import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.stories.Story
import kotlinx.coroutines.flow.Flow

interface RepositoryStories {
    suspend fun getStories(): Flow<SearchResultData<List<Story>>>
    suspend fun getStoryById(storyId: Int): Flow<SearchResultData<Story>>
    fun getUnviewedStories(): List<Int>
    fun setStoryViewed(storyId: Int)
}
