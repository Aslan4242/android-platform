package com.example.androidplatform.ui.cards

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androidplatform.ui.credit_cards.CreditCardsFragment
import com.example.androidplatform.ui.debit_cards.DebitCardsFragment

class CardViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return CARDS_TAB_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DebitCardsFragment.newInstance()
            else -> CreditCardsFragment.newInstance()
        }
    }

    companion object {
        private const val CARDS_TAB_COUNT = 2
    }
}
