package com.example.androidplatform.presentation.stories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.StoriesInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.stories.models.StoriesScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoriesViewModel(
    private val storiesInteractor: StoriesInteractor
) : ViewModel() {

    private var _screenState = MutableLiveData<StoriesScreenState>()
    fun screenState(): LiveData<StoriesScreenState> = _screenState

    fun getStoryById(storyId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = storiesInteractor.getStoryById(storyId)
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value =
                            StoriesScreenState.Content(data.value!!)

                        is SearchResultData.ErrorServer -> _screenState.value =
                            StoriesScreenState.Error(data.message)

                        is SearchResultData.NoInternet -> _screenState.value =
                            StoriesScreenState.Error(data.message)

                        is SearchResultData.Empty -> _screenState.value =
                            StoriesScreenState.Empty(data.message)
                    }
                }
            }
        }
    }
}
