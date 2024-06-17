package com.example.androidplatform.presentation.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.data.network.AuthRequest
import com.example.androidplatform.domain.api.AuthenticationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.authentication.models.StateAuthentication
import com.example.androidplatform.presentation.dashboard.models.DashboardState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashBoardViewModel() : ViewModel() {

    private val _screenState = MutableLiveData<DashboardState>()
    fun screenState(): LiveData<DashboardState> = _screenState

}
