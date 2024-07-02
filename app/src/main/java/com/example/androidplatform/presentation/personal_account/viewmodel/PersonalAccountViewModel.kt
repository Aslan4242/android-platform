package com.example.androidplatform.presentation.personal_account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.ClientInteractor
import com.example.androidplatform.domain.api.LogoutInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.logout.models.LogoutState
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonalAccountViewModel(
    private val clientInteractor: ClientInteractor,
    private val logoutInteractor: LogoutInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenStateClients>()
    fun screenState(): LiveData<ScreenStateClients> = _screenState

    private val _logoutScreenState = MutableLiveData<LogoutState>()
    fun logoutScreenState(): LiveData<LogoutState> = _logoutScreenState
    private val _showToastMessage = SingleLiveEvent<String>()
    val showToastMessage: LiveData<String> = _showToastMessage

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

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = logoutInteractor.logout()
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _logoutScreenState.value =
                            LogoutState.Content(CLIENT_LOGGED_OUT)

                        is SearchResultData.ErrorServer -> {
                            _logoutScreenState.value = LogoutState.Error(data.message)
                            _showToastMessage.postValue(data.description)
                        }

                        is SearchResultData.NoInternet -> {
                            _logoutScreenState.value = LogoutState.Error(data.message)
                            _showToastMessage.postValue(NO_INTERNET)
                        }

                        is SearchResultData.Empty -> {
                            _logoutScreenState.value = LogoutState.Error(data.message)
                            _showToastMessage.postValue(NO_INTERNET)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val NO_INTERNET = "Нет интернета"
        const val CLIENT_LOGGED_OUT = "Вы вышли из аккаунта"
    }
}