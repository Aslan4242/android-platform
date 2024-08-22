package com.example.androidplatform.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.androidplatform.R
import com.example.androidplatform.domain.models.cards.Card

class CardsExpandableListAdapter(
    private val context: Context,
    private val listGroupTitle: String,
    private val listItems: List<Card>
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
            view = LayoutInflater.from(parent?.context).inflate(R.layout.card_view, null)
        }
        val cardImage = view?.findViewById<ImageView>(R.id.cardImage)
        val cardNumber = view?.findViewById<TextView>(R.id.cardNumber)
        val month = view?.findViewById<TextView>(R.id.month)
        val year = view?.findViewById<TextView>(R.id.year)
        val card = getChild(groupPosition, childPosition) as Card
        cardImage?.setImageResource(getCardImage(card.cardProgram))
        cardNumber?.text = card.number
        month?.text = card.month
        year?.text = card.year
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private fun getCardImage(cardProgram: String): Int {
        context.resources.apply {
            return when (cardProgram) {
                getString(R.string.mir) -> R.drawable.mir
                getString(R.string.visa) -> R.drawable.visa
                getString(R.string.maestro) -> R.drawable.maestro
                getString(R.string.mastercard) -> R.drawable.mastercard
                else -> 0
            }
        }

    }
}
