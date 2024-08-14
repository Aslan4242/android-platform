package com.example.androidplatform.presentation.transaction_info.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.HistoryInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.presentation.transaction_info.models.TransactionInfoState
import com.example.androidplatform.util.parseDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class TransactionInfoViewModel(private val historyInteractor: HistoryInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<TransactionInfoState>()
    fun screenState(): LiveData<TransactionInfoState> = _screenState

    fun getTransactionInfo(transactionId: Int) {
        _screenState.value = TransactionInfoState.IsLoading
        viewModelScope.launch(Dispatchers.IO) {
            val historyList = historyInteractor.getTransaction(transactionId)
            historyList.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value =
                            TransactionInfoState.Content(data.value!!)

                        is SearchResultData.Empty -> _screenState.value =
                            TransactionInfoState.Error(data.message)

                        is SearchResultData.ErrorServer -> _screenState.value =
                            TransactionInfoState.Error(data.message)

                        is SearchResultData.NoInternet -> _screenState.value =
                            TransactionInfoState.Error(data.message)
                    }
                }
            }
        }
    }

    fun convertDateTime(input: String): String {
        val dateTime = input.parseDate()
        val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        return dateTime.format(outputFormatter)
    }
}
