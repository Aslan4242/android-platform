package com.example.androidplatform.domain.impl.stories

import com.example.androidplatform.domain.RepositoryStories
import com.example.androidplatform.domain.api.StoriesInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.stories.Story
import kotlinx.coroutines.flow.Flow

class StoriesInteractorImpl(private val storiesRepository: RepositoryStories) : StoriesInteractor {
    override suspend fun getStories(): Flow<SearchResultData<List<Story>>> {
        return storiesRepository.getStories()
    }

    override suspend fun getStoryById(storyId: Int): Flow<SearchResultData<Story>> {
        return storiesRepository.getStoryById(storyId)
    }
}
