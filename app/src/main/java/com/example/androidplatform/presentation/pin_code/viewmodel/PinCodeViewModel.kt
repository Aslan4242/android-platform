package com.example.androidplatform.presentation.pin_code.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.data.network.cards.ActivateCardRequest
import com.example.androidplatform.domain.api.CardsInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.pin_code.models.ActivationState
import kotlinx.coroutines.launch

class PinCodeViewModel(
    private val cardsInteractor: CardsInteractor
) : ViewModel() {

    private val _activationState = MutableLiveData<ActivationState>()
    fun activationState(): LiveData<ActivationState> = _activationState

    fun activateCardById(cardId: Int, cardRequest: ActivateCardRequest) {
        viewModelScope.launch {
            val result = cardsInteractor.activateCardById(cardId, cardRequest)
            result.collect { data ->
                when (data) {
                    is SearchResultData.Data -> _activationState.value =
                        ActivationState.Content(data.value!!)
                    is SearchResultData.Empty -> _activationState.value =
                        ActivationState.Error(data.message)
                    is SearchResultData.ErrorServer -> _activationState.value =
                        ActivationState.Error(data.message)
                    is SearchResultData.NoInternet -> _activationState.value =
                        ActivationState.Error(data.message)
                }
            }
        }
    }
}
