package com.example.androidplatform.data.network.stories

import com.example.androidplatform.R
import com.example.androidplatform.domain.RepositoryStories
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.stories.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryStoriesImpl : RepositoryStories {

    private val storiesList = listOf(
        Story(0, "News", "The latest news update.", R.drawable.ic_launcher_background),
        Story(1, "Update", "New feature added.", R.drawable.ic_launcher_background),
        Story(2, "Reminder", "Don't forget to save your work.", R.drawable.ic_launcher_background),
        Story(3, "Alert", "System needs maintenance.", R.drawable.ic_launcher_background),
        Story(4, "Notice", "Server will be down for maintenance.", R.drawable.ic_launcher_background),
        Story(5, "Tutorial", "How to use the new feature.", R.drawable.ic_launcher_background),
        Story(6, "FAQ", "Frequently asked questions about the app.", R.drawable.ic_launcher_background),
        Story(7, "Tip", "Shortcut to quickly open settings.", R.drawable.ic_launcher_background),
        Story(8, "Announcement", "Upcoming changes to the platform.", R.drawable.ic_launcher_background),
        Story(9, "Warning", "Security update required.", R.drawable.ic_launcher_background)
    )

    override suspend fun getStories(): Flow<SearchResultData<List<Story>>> = flow {
        emit(SearchResultData.Data(storiesList))
    }

    override suspend fun getStoryById(storyId: Int): Flow<SearchResultData<Story>> = flow {
        emit(SearchResultData.Data(storiesList[storyId]))
    }
}
