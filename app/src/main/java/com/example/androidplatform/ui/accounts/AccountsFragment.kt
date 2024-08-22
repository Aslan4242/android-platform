package com.example.androidplatform.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentAccountsBinding
import com.example.androidplatform.presentation.cards.models.CardsState
import com.example.androidplatform.presentation.cards.viewmodel.CardsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountsFragment : Fragment()  {
    private var _binding: FragmentAccountsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CardsViewModel>()
    private lateinit var tabLayoutMediator: TabLayoutMediator


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.accountViewPager.adapter = AccountsViewPagerAdapter(childFragmentManager, lifecycle)

        tabLayoutMediator = TabLayoutMediator(binding.accountTabLayout, binding.accountViewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.saving_account)
                1 -> tab.text = getString(R.string.current_account)
            }
        }
        tabLayoutMediator.attach()
    }

    private fun render(state: CardsState) {
        when (state) {
            is CardsState.Content -> {
            }
            else -> {}
        }
    }
}
