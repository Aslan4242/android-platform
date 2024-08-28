package com.example.androidplatform.presentation.account_transfer.models

import com.example.androidplatform.domain.models.account.Account

sealed interface AccountTransferScreenState {
    object AccountTransferScreen: AccountTransferScreenState
    data class BottomSheet(val accounts: List<Account>) : AccountTransferScreenState
}
