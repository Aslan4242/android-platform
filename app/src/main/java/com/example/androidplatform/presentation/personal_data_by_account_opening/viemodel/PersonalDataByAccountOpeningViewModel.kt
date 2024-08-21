package com.example.androidplatform.presentation.personal_data_by_account_opening.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.R
import com.example.androidplatform.data.network.launch_operation.OperationCode
import com.example.androidplatform.data.network.proceed_operation.ProceedOperationRequestItem
import com.example.androidplatform.data.network.proceed_operation.account_opening.AccountType
import com.example.androidplatform.domain.api.ClientInteractor
import com.example.androidplatform.domain.api.ConfirmOperationInteractor
import com.example.androidplatform.domain.api.LaunchOperationInteractor
import com.example.androidplatform.domain.api.ProceedOperationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.personal_data_by_card_ordering.models.OperationState
import com.example.androidplatform.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class PersonalDataByAccountOpeningViewModel(
    private val clientInteractor: ClientInteractor,
    private val launchOperationInteractor: LaunchOperationInteractor,
    private val proceedOperationInteractor: ProceedOperationInteractor,
    private val confirmOperationInteractor: ConfirmOperationInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenStateClients>()
    fun screenState(): LiveData<ScreenStateClients> = _screenState

    private val _operationState = MutableLiveData<OperationState>()
    fun operationState(): LiveData<OperationState> = _operationState

    private val _showToastMessage = SingleLiveEvent<String>()
    val showToastMessage: LiveData<String> = _showToastMessage

    fun getClients() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = clientInteractor.getClients()
            result.collect { data ->
                withContext(Dispatchers.Main) {
                    handleScreenState(data)
                }
            }
        }
    }

    fun openAccount(accountType: AccountType, currencyType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val launchOperationResult =
                launchOperationInteractor.launchOperation(OperationCode.ACCOUNT_OPEN)
            launchOperationResult.collect { data ->
                when (data) {
                    is SearchResultData.Data -> {
                        val requestId = data.value!!.requestId
                        val proceedOperationResult = proceedOperationInteractor.proceedOperation(
                            requestId,
                            arrayListOf(
                                ProceedOperationRequestItem(
                                    ACCOUNT_TYPE,
                                    accountType.description
                                ),
                                ProceedOperationRequestItem(
                                    CURRENCY,
                                    currencyType
                                )
                            )
                        )
                        val confirmOperationResult =
                            confirmOperationInteractor.confirmOperation(requestId)
                        withContext(Dispatchers.Main) {
                            proceedOperationResult.collect { proceedData ->
                                handleOperationState(proceedData)
                            }
                            confirmOperationResult.collect { confirmData ->
                                handleOperationState(confirmData)
                            }
                        }
                    }

                    is SearchResultData.ErrorServer,
                    is SearchResultData.NoInternet,
                    is SearchResultData.Empty -> {
                        withContext(Dispatchers.Main) {
                            handleOperationState(data)
                        }
                    }
                }
            }
        }
    }

    fun convertDateTime(input: String): String {
        return parseDate(input).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    private fun parseDate(input: String): LocalDateTime {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .optionalStart()
            .appendPattern("'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 3, true)
            .optionalEnd()
            .optionalEnd()
            .toFormatter()
        return LocalDateTime.parse(input, formatter)
    }

    fun getGenderId(gender: String): Int {
        return when (gender) {
            "Male" -> R.id.male_radio_button
            "Female" -> R.id.female_radio_button
            else -> 0
        }
    }

    private fun handleScreenState(data: SearchResultData<Client>) {
        _screenState.value = when (data) {
            is SearchResultData.Data -> ScreenStateClients.Content(data.value!!)
            is SearchResultData.ErrorServer -> ScreenStateClients.Error(data.message)
            is SearchResultData.NoInternet -> ScreenStateClients.NoInternet(data.message)
            is SearchResultData.Empty -> ScreenStateClients.Empty(data.message)
        }
    }

    private fun handleOperationState(data: SearchResultData<OperationItem>) {
        _operationState.value = when (data) {
            is SearchResultData.Data -> OperationState.Content(data.value!!)
            is SearchResultData.ErrorServer -> OperationState.Error(data.message)
            is SearchResultData.NoInternet -> OperationState.NoInternet(data.message)
            is SearchResultData.Empty -> OperationState.Empty(data.message)
        }
    }

    companion object {
        private const val ACCOUNT_TYPE = "AccountType"
        private const val CURRENCY = "Currency"
    }
}
