package com.example.androidplatform.presentation.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidplatform.presentation.dashboard.models.DashboardState

class DashBoardViewModel() : ViewModel() {

    private val _screenState = MutableLiveData<DashboardState>()
    fun screenState(): LiveData<DashboardState> = _screenState

}
