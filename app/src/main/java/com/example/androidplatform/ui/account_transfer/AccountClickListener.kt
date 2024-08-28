package com.example.androidplatform.ui.account_transfer

import com.example.androidplatform.domain.models.account.Account

interface AccountClickListener {
    fun onAccountClick(account: Account)
}
