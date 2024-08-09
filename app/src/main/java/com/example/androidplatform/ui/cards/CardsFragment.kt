package com.example.androidplatform.ui.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentCardsBinding
import com.example.androidplatform.presentation.cards.models.CardsState
import com.example.androidplatform.presentation.cards.viewmodel.CardsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardsFragment : Fragment()  {
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CardsViewModel>()
    private lateinit var tabLayoutMediator: TabLayoutMediator


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.cardViewPager.adapter = CardViewPagerAdapter(childFragmentManager, lifecycle)

        tabLayoutMediator = TabLayoutMediator(binding.cardTabLayout, binding.cardViewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.debit_card)
                1 -> tab.text = getString(R.string.credit_card)
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