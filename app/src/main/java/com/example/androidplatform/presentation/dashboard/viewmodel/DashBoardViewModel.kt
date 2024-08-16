package com.example.androidplatform.presentation.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.CardsInteractor
import com.example.androidplatform.domain.api.StoriesInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.dashboard.models.ScreenStateCards
import com.example.androidplatform.presentation.dashboard.models.StoriesListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashBoardViewModel(
    private val storiesInteractor: StoriesInteractor,
    private val cardsInteractor: CardsInteractor
) : ViewModel() {

    private val _storiesListState = MutableLiveData<StoriesListState>()
    fun storiesListState(): LiveData<StoriesListState> = _storiesListState

    private val _cardsScreenState = MutableLiveData<ScreenStateCards>()
    fun cardsScreenState(): LiveData<ScreenStateCards> = _cardsScreenState

    fun getCards() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = cardsInteractor.getCards()
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _cardsScreenState.value =
                            ScreenStateCards.Content(data.value!!)

                        is SearchResultData.ErrorServer -> _cardsScreenState.value =
                            ScreenStateCards.Error(data.message)

                        is SearchResultData.NoInternet -> _cardsScreenState.value =
                            ScreenStateCards.Error(data.message)

                        is SearchResultData.Empty -> _cardsScreenState.value =
                            ScreenStateCards.Empty(data.message)
                    }
                }
            }
        }
    }

    fun getStories() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = storiesInteractor.getStories()
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _storiesListState.value =
                            StoriesListState.Content(data.value!!)

                        is SearchResultData.ErrorServer -> _storiesListState.value =
                            StoriesListState.Error(data.message)

                        is SearchResultData.NoInternet -> _storiesListState.value =
                            StoriesListState.Error(data.message)

                        is SearchResultData.Empty -> _storiesListState.value =
                            StoriesListState.Error(data.message)
                    }
                }
            }
        }
    }
}
