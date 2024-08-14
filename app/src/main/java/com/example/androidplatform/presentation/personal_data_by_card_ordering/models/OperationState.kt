package com.example.androidplatform.presentation.personal_data_by_card_ordering.models

import androidx.annotation.StringRes
import com.example.androidplatform.domain.models.launch_operation.OperationItem

sealed interface OperationState {
    object IsLoading : OperationState
    data class Content(val operationItem: OperationItem) : OperationState
    data class Error(@StringRes val message: Int) : OperationState
    data class Empty(@StringRes val message: Int) : OperationState
    data class NoInternet(@StringRes val message: Int) : OperationState
}
