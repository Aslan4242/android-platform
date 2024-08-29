package com.example.androidplatform.ui.account_transfer

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplatform.domain.models.account.Account

class BottomSheetAdapter(private val items: List<Account>) :
    RecyclerView.Adapter<BottomSheetViewHolder>() {
    private val itemList = ArrayList(this.items)

    var accountClickListener: AccountClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val account = itemList[position]
        holder.bind(account)
        holder.itemView.setOnClickListener { accountClickListener?.onAccountClick(account) }
    }

    fun addItems(values: List<Account>) {
        this.itemList.clear()
        if (values.isNotEmpty()) {
            this.itemList.addAll(values)
        }
        this.notifyDataSetChanged()
    }

    fun interface WriteOffAccountClickListener : AccountClickListener {
        override fun onAccountClick(account: Account)
    }

    fun interface RecipientAccountClickListener : AccountClickListener {
        override fun onAccountClick(account: Account)
    }
}
