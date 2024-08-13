package com.example.androidplatform.presentation.history.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.androidplatform.databinding.ItemHistoryExpenseBlockBinding
import com.example.androidplatform.presentation.history.models.HistoryExpenseBlockData

class HistoryExpenseBlockViewHolder(
    private val binding: ItemHistoryExpenseBlockBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(historyExpenseBlockData: HistoryExpenseBlockData) {
        binding.fullNameTv.setText(historyExpenseBlockData.header)
        binding.spendingValue.text = historyExpenseBlockData.text
    }
}