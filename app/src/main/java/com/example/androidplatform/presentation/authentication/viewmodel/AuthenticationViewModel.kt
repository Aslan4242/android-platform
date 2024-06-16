package com.example.androidplatform.presentation.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.data.network.AuthRequest
import com.example.androidplatform.domain.api.AuthenticationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.authentication.models.StateAuthentication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel(
    private val authenticationInteractor: AuthenticationInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<StateAuthentication>()
    fun screenState(): LiveData<StateAuthentication> = _screenState

    fun authenticate(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO)  {
            val result = authenticationInteractor.authentication(AuthRequest(login, password))
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value = StateAuthentication.Content(
                            data.value?.authenticationItem!!
                        )
                        is SearchResultData.ErrorServer -> _screenState.value = StateAuthentication.Error(data.message)
                        is SearchResultData.NoInternet -> _screenState.value = StateAuthentication.Error(data.message)
                        is SearchResultData.Empty -> _screenState.value = StateAuthentication.Error(data.message)
                    }
                }
            }
        }
    }
}
