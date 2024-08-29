package com.example.androidplatform.presentation.confirm_transfer_operation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidplatform.data.network.launch_operation.OperationCode
import com.example.androidplatform.data.network.proceed_operation.ProceedOperationRequestItem
import com.example.androidplatform.domain.api.ConfirmOperationInteractor
import com.example.androidplatform.domain.api.LaunchOperationInteractor
import com.example.androidplatform.domain.api.ProceedOperationInteractor
import com.example.androidplatform.domain.models.SearchResultData
import com.example.androidplatform.domain.models.launch_operation.OperationItem
import com.example.androidplatform.presentation.confirm_transfer_operation.models.ConfirmTransferOperationState
import com.example.androidplatform.presentation.personal_data_by_card_ordering.models.OperationState
import com.example.androidplatform.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmTransferOperationViewModel(
    application: Application,
    private val launchOperationInteractor: LaunchOperationInteractor,
    private val proceedOperationInteractor: ProceedOperationInteractor,
    private val confirmOperationInteractor: ConfirmOperationInteractor
) : AndroidViewModel(application) {

    private val _screenState = MutableLiveData<ConfirmTransferOperationState>()
    fun screenState(): LiveData<ConfirmTransferOperationState> = _screenState

    private val _operationState = MutableLiveData<OperationState>()
    fun operationState(): LiveData<OperationState> = _operationState

    private val _showToastMessage = SingleLiveEvent<String>()
    val showToastMessage: LiveData<String> = _showToastMessage

    fun transfer(writeOffAccountNumber: String, recipientAccountNumber: String, sum: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val launchOperationResult =
                launchOperationInteractor.launchOperation(OperationCode.ACCOUNT_TRANSFER)
            launchOperationResult.collect { data ->
                handleLaunchOperationResult(
                    data,
                    writeOffAccountNumber,
                    recipientAccountNumber,
                    sum
                )
            }
        }
    }

    private suspend fun handleLaunchOperationResult(
        data: SearchResultData<OperationItem>,
        writeOffAccountNumber: String,
        recipientAccountNumber: String,
        sum: String
    ) {
        when (data) {
            is SearchResultData.Data -> proceedOperation(
                data.value!!.requestId,
                writeOffAccountNumber,
                recipientAccountNumber,
                sum
            )

            else -> handleError(data)
        }
    }

    private suspend fun proceedOperation(
        requestId: Int,
        writeOffAccountNumber: String,
        recipientAccountNumber: String,
        sum: String
    ) {
        val proceedOperationResult = proceedOperationInteractor.proceedOperation(
            requestId,
            arrayListOf(
                ProceedOperationRequestItem(SOURCE_ACCOUNT, "[$writeOffAccountNumber]"),
                ProceedOperationRequestItem(RECEIVER_ACCOUNT, recipientAccountNumber),
                ProceedOperationRequestItem(AMOUNT, sum)
            )
        )

        withContext(Dispatchers.Main) {
            proceedOperationResult.collect { proceedData ->
                handleProceedOperationResult(proceedData, requestId)
            }
        }
    }

    private suspend fun handleProceedOperationResult(
        proceedData: SearchResultData<OperationItem>,
        requestId: Int
    ) {
        when (proceedData) {
            is SearchResultData.Data -> confirmOperation(requestId)
            else -> handleError(proceedData)
        }
    }

    private suspend fun confirmOperation(requestId: Int) {
        val confirmOperationResult = confirmOperationInteractor.confirmOperation(requestId)

        withContext(Dispatchers.Main) {
            confirmOperationResult.collect { confirmData ->
                handleOperationState(confirmData)
            }
        }
    }

    private fun handleError(data: SearchResultData<OperationItem>) {
        val message = when (data) {
            is SearchResultData.ErrorServer -> data.description
            is SearchResultData.NoInternet -> getApplication<Application>().resources.getString(data.message)
            is SearchResultData.Empty -> getApplication<Application>().resources.getString(data.message)
            else -> ""
        }
        _showToastMessage.postValue(message)
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
        const val SOURCE_ACCOUNT = "SourceAccount"
        const val RECEIVER_ACCOUNT = "ReceiverAccount"
        const val AMOUNT = "Amount"
    }
}
