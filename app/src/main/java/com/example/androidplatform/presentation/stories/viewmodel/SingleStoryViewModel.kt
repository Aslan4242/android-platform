package com.example.androidplatform.presentation.stories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.StoriesInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.stories.models.SingleStoryScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SingleStoryViewModel(
    private val storiesInteractor: StoriesInteractor
) : ViewModel() {

    private var _screenState = MutableLiveData<SingleStoryScreenState>()
    fun screenState(): LiveData<SingleStoryScreenState> = _screenState

    fun getStoryById(storyId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = storiesInteractor.getStoryById(storyId)
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value =
                            SingleStoryScreenState.Content(data.value!!)

                        is SearchResultData.ErrorServer -> _screenState.value =
                            SingleStoryScreenState.Error(data.message)

                        is SearchResultData.NoInternet -> _screenState.value =
                            SingleStoryScreenState.Error(data.message)

                        is SearchResultData.Empty -> _screenState.value =
                            SingleStoryScreenState.Empty(data.message)
                    }
                }
            }
        }
    }

    fun setStoryViewed(storyId: Int) = storiesInteractor.setStoryViewed(storyId)
}
