package com.example.androidplatform.ui.accounts

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androidplatform.ui.current_account.CurrentAccountFragment
import com.example.androidplatform.ui.saving_account.SavingAccountFragment

class AccountsViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return ACCOUNTS_TAB_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavingAccountFragment.newInstance()
            else -> CurrentAccountFragment.newInstance()
        }
    }

    companion object {
        private const val ACCOUNTS_TAB_COUNT = 2
    }
}
