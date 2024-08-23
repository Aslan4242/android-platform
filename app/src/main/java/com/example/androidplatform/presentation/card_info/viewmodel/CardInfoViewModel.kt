package com.example.androidplatform.presentation.card_info.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.CardsInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.card_info.models.CardCvcState
import com.example.androidplatform.presentation.card_info.models.CardInfoState
import com.example.androidplatform.presentation.card_info.models.CardLockState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardInfoViewModel(
    private val cardsInteractor: CardsInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<CardInfoState>()
    fun screenState(): LiveData<CardInfoState> = _screenState

    private val _cvcState = MutableLiveData<CardCvcState>()
    fun cvcState(): LiveData<CardCvcState> = _cvcState

    private val _lockState = MutableLiveData<CardLockState>()
    fun lockState(): LiveData<CardLockState> = _lockState

    fun getCard(cardId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = cardsInteractor.getCards()
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value =
                            CardInfoState.Content(data.value!!.first { card -> card.id == cardId })
                        is SearchResultData.Empty -> _screenState.value =
                            CardInfoState.Error(data.message)
                        is SearchResultData.ErrorServer -> _screenState.value =
                            CardInfoState.Error(data.message)
                        is SearchResultData.NoInternet -> _screenState.value =
                            CardInfoState.Error(data.message)
                    }
                }
            }
        }
    }

    fun getCardCvc(cardId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = cardsInteractor.getCardCvc(cardId)
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _cvcState.value =
                            CardCvcState.Content(data.value!!)
                        is SearchResultData.Empty -> _cvcState.value =
                            CardCvcState.Error(data.message)
                        is SearchResultData.ErrorServer -> _cvcState.value =
                            CardCvcState.Error(data.message)
                        is SearchResultData.NoInternet -> _cvcState.value =
                            CardCvcState.Error(data.message)
                    }
                }
            }
        }
    }

    fun blockCardById(cardId: Int) {
        viewModelScope.launch {
            val result = cardsInteractor.lockCardById(cardId)
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _lockState.value =
                            CardLockState.Content(data.value!!)
                        is SearchResultData.Empty -> _lockState.value =
                            CardLockState.Error(data.message)
                        is SearchResultData.ErrorServer -> _lockState.value =
                            CardLockState.Error(data.message)
                        is SearchResultData.NoInternet -> _lockState.value =
                            CardLockState.Error(data.message)
                    }
                }
            }
        }
    }

    fun unlockCardById(cardId: Int) {
        viewModelScope.launch {
            val result = cardsInteractor.unlockCardById(cardId)
            result.collect { data ->
                when (data) {
                    is SearchResultData.Data -> _lockState.value =
                        CardLockState.Content(data.value!!)
                    is SearchResultData.Empty -> _lockState.value =
                        CardLockState.Error(data.message)
                    is SearchResultData.ErrorServer -> _lockState.value =
                        CardLockState.Error(data.message)
                    is SearchResultData.NoInternet -> _lockState.value =
                        CardLockState.Error(data.message)
                }
            }
        }
    }
}
