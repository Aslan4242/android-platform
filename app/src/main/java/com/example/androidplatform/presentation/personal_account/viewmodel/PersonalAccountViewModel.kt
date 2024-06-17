package com.example.androidplatform.presentation.personal_account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.ClientInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonalAccountViewModel(
    private val clientInteractor: ClientInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenStateClients>()
    fun screenState(): LiveData<ScreenStateClients> = _screenState

    fun getClients() {
        viewModelScope.launch(Dispatchers.IO)  {
            val result = clientInteractor.getClients()
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value = ScreenStateClients.Content(data.value!!)
                        is SearchResultData.ErrorServer -> _screenState.value = ScreenStateClients.Error(data.message)
                        is SearchResultData.NoInternet -> _screenState.value = ScreenStateClients.Error(data.message)
                        is SearchResultData.Empty -> _screenState.value = ScreenStateClients.Error(data.message)
                    }
                }
            }
        }
    }
}