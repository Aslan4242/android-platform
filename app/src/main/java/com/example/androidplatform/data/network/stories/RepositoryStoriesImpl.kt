package com.example.androidplatform.data.network.stories

import com.example.androidplatform.R
import com.example.androidplatform.domain.RepositoryStories
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.stories.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryStoriesImpl : RepositoryStories {

    private val storiesList = listOf(
        Story(0, "News", "The latest news update.", R.drawable.story_background_1),
        Story(1, "Update", "New feature added.", R.drawable.story_background_2),
        Story(2, "Reminder", "Don't forget to save your work.", R.drawable.story_background_3),
        Story(3, "Alert", "System needs maintenance.", R.drawable.story_background_2),
        Story(4, "Notice", "Server will be down for maintenance.", R.drawable.story_background_1),
        Story(5, "Tutorial", "How to use the new feature.", R.drawable.story_background_3),
        Story(6, "FAQ", "Frequently asked questions about the app.", R.drawable.story_background_2),
        Story(7, "Tip", "Shortcut to quickly open settings.", R.drawable.story_background_3),
        Story(8, "Announcement", "Upcoming changes to the platform.", R.drawable.story_background_1),
        Story(9, "Warning", "Security update required.", R.drawable.story_background_3)
    )
    private val unviewedStories = storiesList.map { it.id }.toMutableList()

    override suspend fun getStories(): Flow<SearchResultData<List<Story>>> = flow {
        emit(SearchResultData.Data(storiesList))
    }

    override suspend fun getStoryById(storyId: Int): Flow<SearchResultData<Story>> = flow {
        emit(SearchResultData.Data(storiesList[storyId]))
    }

    override fun getUnviewedStories(): List<Int> = unviewedStories

    override fun setStoryViewed(storyId: Int) {
        unviewedStories.remove(storyId)
    }
}
