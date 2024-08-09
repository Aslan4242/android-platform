package com.example.androidplatform.presentation.cards.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidplatform.presentation.cards.models.CardsState

class CardsViewModel() : ViewModel() {

    private val _screenState = MutableLiveData<CardsState>()
    fun screenState(): LiveData<CardsState> = _screenState

}
