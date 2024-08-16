package com.example.androidplatform.presentation.history.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.domain.api.HistoryInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.account.Currency
import com.example.androidplatform.domain.models.history.Transaction
import com.example.androidplatform.domain.models.history.TransactionType
import com.example.androidplatform.presentation.history.models.HistoryState
import com.example.androidplatform.util.SingleLiveEvent
import com.example.androidplatform.util.parseDate
import com.example.androidplatform.util.toCurrencyMoneyFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.format.DateTimeFormatter

class HistoryViewModel(private val historyInteractor: HistoryInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<HistoryState>()
    fun screenState(): LiveData<HistoryState> = _screenState

    private val _showToastMessage = SingleLiveEvent<String>()
    val showToastMessage: LiveData<String> = _showToastMessage

    fun getHistory() {
        _screenState.value = HistoryState.IsLoading
        viewModelScope.launch(Dispatchers.IO) {
            val historyList = historyInteractor.getHistory()
            historyList.collect { data ->
                withContext(Dispatchers.Main) {
                    when (data) {
                        is SearchResultData.Data -> _screenState.value =
                            HistoryState.Content(data.value!!)

                        is SearchResultData.Empty -> _screenState.value =
                            HistoryState.Error(data.message)

                        is SearchResultData.ErrorServer -> _screenState.value =
                            HistoryState.Error(data.message)

                        is SearchResultData.NoInternet -> _screenState.value =
                            HistoryState.Error(data.message)
                    }
                }
            }
        }
    }

    fun getTransactionSum(transactions: List<Transaction>) = transactions
        .filter { transaction -> transaction.type == TransactionType.EXPENSE.value }
        .map { transaction -> transaction.amount }
        .sum()
        .toString()
        .toCurrencyMoneyFormat(Currency.RUB.code)

    fun groupTransactionsByDate(transactions: List<Transaction>) = transactions
        .groupBy { transaction -> convertDateTime(transaction.date) }
        .toSortedMap(Comparator.reverseOrder())
        .flatMap { entry ->
            mutableListOf<Any>(entry.key)
                .apply { addAll(entry.value) }
        }

    private fun convertDateTime(input: String): String {
        val dateTime = input.parseDate()
        val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return dateTime.format(outputFormatter)
    }
}
