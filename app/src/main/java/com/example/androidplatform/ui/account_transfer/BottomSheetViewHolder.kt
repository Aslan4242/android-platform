package com.example.androidplatform.ui.account_transfer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplatform.R
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.util.toCurrencyMoneyFormat

class BottomSheetViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.accounts_list, parentView, false)
) {
    private val accountNumberView: TextView by lazy { itemView.findViewById(R.id.account_number_tv)}
    private val accountBalanceView: TextView by lazy { itemView.findViewById(R.id.balance_tv)}

    fun bind(model: Account) {
        accountNumberView.text = model.number
        accountBalanceView.text = model.balance.toString().toCurrencyMoneyFormat(model.currency)
    }
}
