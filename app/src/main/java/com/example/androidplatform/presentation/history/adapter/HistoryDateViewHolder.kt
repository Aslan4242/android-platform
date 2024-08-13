package com.example.androidplatform.presentation.history.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.androidplatform.databinding.ItemHistoryDateBinding

class HistoryDateViewHolder(
    private val binding: ItemHistoryDateBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(date: String) {
        binding.date.text = date
    }
}