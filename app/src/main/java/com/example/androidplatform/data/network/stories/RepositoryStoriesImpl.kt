package com.example.androidplatform.data.network.stories

import com.example.androidplatform.R
import com.example.androidplatform.domain.RepositoryStories
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.stories.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryStoriesImpl : RepositoryStories {

    private val storiesList = listOf(
        Story(1, "Баги бывают разные", "Один баг может привести к падению приложения, другой - к неправильному отображению данных, а третий - к уязвимости системы.", R.drawable.story_background_1),
        Story(2, "Тестирование - это не только поиск ошибок", "Тестирование также помогает подтвердить, что приложение работает так, как задумано, и соответствует требованиям.", R.drawable.story_background_2),
        Story(3, "Автоматизация - это не панацея", "Автоматизация тестирования ускоряет процесс, но ручное тестирование все еще необходимо для некоторых видов проверок.", R.drawable.story_background_3),
        Story(4, "Тестирование начинается с требований", "Чем четче сформулированы требования, тем легче написать тесты.", R.drawable.story_background_2),
        Story(5, "Пирамида тестирования", "Пирамида тестирования помогает визуализировать соотношение различных типов тестов в проекте.", R.drawable.story_background_1),
        Story(6, "Модульное тестирование", "Модульное тестирование позволяет изолировать и тестировать отдельные компоненты системы.", R.drawable.story_background_3),
        Story(7,  "Интеграционное тестирование", "Интеграционное тестирование проверяет взаимодействие между различными компонентами системы.", R.drawable.story_background_2),
        Story(8,  "Регрессионное тестирование", "Регрессионное тестирование гарантирует, что новые изменения не сломали уже существующий функционал.", R.drawable.story_background_3),
        Story(9,  "Тестирование производительности", "Тестирование производительности позволяет оценить, насколько быстро работает приложение.", R.drawable.story_background_1),
        Story(10, "Тестирование безопасности", "Тестирование безопасности помогает выявить уязвимости в системе, которые могут быть использованы злоумышленниками.", R.drawable.story_background_3)
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
