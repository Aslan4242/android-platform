package com.example.androidplatform.presentation.confirm_transfer_operation.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.launch_operation.OperationItem

sealed interface ConfirmTransferOperationState {
    object IsLoading : ConfirmTransferOperationState
    data class Content(val operationItem: OperationItem) : ConfirmTransferOperationState
    data class Error(@StringRes val message: Int) : ConfirmTransferOperationState
    data class Empty(@StringRes val message: Int) : ConfirmTransferOperationState
    data class NoInternet(@StringRes val message: Int) : ConfirmTransferOperationState
}
