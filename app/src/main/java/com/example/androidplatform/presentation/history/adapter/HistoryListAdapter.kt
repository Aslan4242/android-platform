package com.example.androidplatform.presentation.history.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.androidplatform.databinding.ItemHistoryDateBinding
import com.example.androidplatform.databinding.ItemHistoryExpenseBlockBinding
import com.example.androidplatform.databinding.ItemHistoryTransactionBinding
import com.example.androidplatform.domain.models.history.Transaction
import com.example.androidplatform.presentation.transaction_info.listeners.OnHistoryItemClickListener
import com.example.androidplatform.presentation.history.models.HistoryExpenseBlockData

class HistoryListAdapter(
    private val onHistoryItemClickListener: OnHistoryItemClickListener
) : ListAdapter<Any, ViewHolder>(HistoryItemDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == DATE) {
            val binding = ItemHistoryDateBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
            return HistoryDateViewHolder(binding)
        } else if (viewType == TRANSACTION) {
            val binding = ItemHistoryTransactionBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
            return HistoryItemViewHolder(binding, onHistoryItemClickListener)
        } else {
            val binding = ItemHistoryExpenseBlockBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
            return HistoryExpenseBlockViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = getItem(position)
        val itemViewType = getItemViewType(position)
        if (itemViewType == DATE) {
            (holder as HistoryDateViewHolder).bind(getItem(position) as String)
        } else if (itemViewType == TRANSACTION) {
            (holder as HistoryItemViewHolder).bind(historyItem as Transaction)
        } else {
            (holder as HistoryExpenseBlockViewHolder).bind(historyItem as HistoryExpenseBlockData)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item is String) {
            return DATE
        } else if (item is Transaction) {
            return TRANSACTION
        } else {
            return EXPENSE_BLOCK
        }
    }

    object HistoryItemDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is String && newItem is String) {
                return oldItem == newItem
            } else if (oldItem is Transaction && newItem is Transaction) {
                return oldItem.id == newItem.id
            } else if (oldItem is HistoryExpenseBlockData && newItem is HistoryExpenseBlockData) {
                return oldItem.text == newItem.text
            }
            return false
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is String && newItem is String) {
                return oldItem == newItem
            } else if (oldItem is Transaction && newItem is Transaction) {
                return oldItem == newItem
            } else if (oldItem is HistoryExpenseBlockData && newItem is HistoryExpenseBlockData) {
                return oldItem == newItem
            }
            return false
        }
    }

    private companion object {
        const val DATE = 0
        const val TRANSACTION = 1
        const val EXPENSE_BLOCK = 2
    }
}
