package com.example.androidplatform.presentation.account_info.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.AccountInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.account_info.models.AccountInfoState
import com.example.androidplatform.presentation.account_info.models.AccountLockState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountInfoViewModel (
    private val accountInteractor: AccountInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<AccountInfoState>()
    fun screenState(): LiveData<AccountInfoState> = _screenState


    private val _lockState = MutableLiveData<AccountLockState>()
    fun lockState(): LiveData<AccountLockState> = _lockState

    fun getAccount(accountId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = accountInteractor.getAccount(accountId)
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value =
                            AccountInfoState.Content(data.value!!)
                        is SearchResultData.Empty -> _screenState.value =
                            AccountInfoState.Error(data.message)
                        is SearchResultData.ErrorServer -> _screenState.value =
                            AccountInfoState.Error(data.message)
                        is SearchResultData.NoInternet -> _screenState.value =
                            AccountInfoState.Error(data.message)
                    }
                }
            }
        }
    }

    fun blockAccountById(accountId: Int) {
        viewModelScope.launch {
            val result = accountInteractor.lockAccountById(accountId)
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _lockState.value =
                            AccountLockState.Content(data.value!!)
                        is SearchResultData.Empty -> _lockState.value =
                            AccountLockState.Error(data.message)
                        is SearchResultData.ErrorServer -> _lockState.value =
                            AccountLockState.Error(data.message)
                        is SearchResultData.NoInternet -> _lockState.value =
                            AccountLockState.Error(data.message)
                    }
                }
            }
        }
    }

    fun unlockAccountById(cardId: Int) {
        viewModelScope.launch {
            val result = accountInteractor.unlockAccountById(cardId)
            result.collect { data ->
                when (data) {
                    is SearchResultData.Data -> _lockState.value =
                        AccountLockState.Content(data.value!!)
                    is SearchResultData.Empty -> _lockState.value =
                        AccountLockState.Error(data.message)
                    is SearchResultData.ErrorServer -> _lockState.value =
                        AccountLockState.Error(data.message)
                    is SearchResultData.NoInternet -> _lockState.value =
                        AccountLockState.Error(data.message)
                }
            }
        }
    }
}
