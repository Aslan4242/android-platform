package com.example.androidplatform.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.FrameLayout
import android.widget.TextView
import com.example.androidplatform.R
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.util.toCurrencyMoneyFormat

class AccountsExpandableListAdapter(
    private val listGroupTitle: String,
    private val listItems: List<Account>,
    private val onAccountClickListener: (cardId: Int) -> Unit
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return 1
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listItems.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return listGroupTitle
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return listItems[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.list_header_view, null)
        }
        val textView = view?.findViewById<TextView>(R.id.header_tv)
        textView?.text = getGroup(groupPosition) as String
        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.account_view, null)
        }
        val accountContainer = view?.findViewById<FrameLayout>(R.id.accountContainer)
        val accountNumber = view?.findViewById<TextView>(R.id.accountNumber)
        val balance = view?.findViewById<TextView>(R.id.balance_tv)
        val account = getChild(groupPosition, childPosition) as Account
        accountNumber?.text = account.number
        balance?.text = account.balance.toString().toCurrencyMoneyFormat(account.currency)
        accountContainer?.setOnClickListener { onAccountClickListener.invoke(account.id) }
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
