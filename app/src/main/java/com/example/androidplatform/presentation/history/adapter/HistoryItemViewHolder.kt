package com.example.androidplatform.presentation.history.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.androidplatform.R
import com.example.androidplatform.databinding.ItemHistoryTransactionBinding
import com.example.androidplatform.domain.models.history.Transaction
import com.example.androidplatform.presentation.transaction_info.listeners.OnHistoryItemClickListener
import com.example.androidplatform.util.toCurrencyMoneyFormat

class HistoryItemViewHolder(
    private val binding: ItemHistoryTransactionBinding,
    private val clickListener: OnHistoryItemClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(transaction: Transaction) {
        binding.transactionBlock.setOnClickListener { _ -> clickListener.onClick(transaction.id) }
        binding.transactionAccount.text = transaction.account.number
        binding.transactionName.text = transaction.comment
        binding.transactionAmount.apply {
            var amountText = transaction.amount.toString()
                .toCurrencyMoneyFormat(transaction.account.currency)
            if (transaction.type == "Income") {
                amountText = "+$amountText"
                setTextColor(resources.getColor(R.color.green))
            } else {
                amountText = "-$amountText"
                setTextColor(resources.getColor(R.color.red_2))
            }
            text = amountText
        }
    }
}
