package com.example.androidplatform.presentation.transaction_info.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.HistoryInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.domain.models.history.Transaction
import com.example.androidplatform.presentation.transaction_info.models.TransactionInfoState
import com.example.androidplatform.util.parseDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class TransactionInfoViewModel(private val historyInteractor: HistoryInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<TransactionInfoState>()
    fun screenState(): LiveData<TransactionInfoState> = _screenState

    fun getTransactionInfo(transactionId: Int) {
        _screenState.value = TransactionInfoState.IsLoading
        viewModelScope.launch(Dispatchers.IO) {
            val historyList = if (transactionId == 0) {
                flowOf(SearchResultData.Data(TRANSACTION))
            } else {
                historyInteractor.getTransaction(transactionId)
            }
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

    companion object {
        private val TRANSACTION = Transaction(
            id = 0,
            account = Account(
                id = 0,
                client = null,
                createdDate = "2024-08-18T12:23:07.757386",
                currency = 643,
                number = "40861156574182180337",
                name = "Текущий счёт",
                balance = 40000,
                state = "Active"
            ),
            receiver = "40835111716751865322",
            date = "2024-08-26T19:56:56.017938",
            paymentDate = "2024-08-26T19:56:56.017938",
            amount = 13000,
            comment = "Платеж за макдак",
            reason = null,
            state = "Completed",
            type = "Expense"
        )
    }
}
