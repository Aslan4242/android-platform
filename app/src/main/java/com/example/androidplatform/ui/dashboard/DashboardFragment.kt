package com.example.androidplatform.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentDashboardBinding
import com.example.androidplatform.domain.models.cards.Card
import com.example.androidplatform.presentation.dashboard.models.ScreenStateCards
import com.example.androidplatform.presentation.dashboard.viewmodel.DashBoardViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment()  {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<DashBoardViewModel>()
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var listData: List<Card> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCards()

        val swipeRefreshLayout = binding.dashboardSrl
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getCards()
            swipeRefreshLayout.isRefreshing = false
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        viewModel.cardsScreenState().observe(viewLifecycleOwner) {
            renderCards(it)
        }

        binding.orderCardBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_cardsFragment)
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.do_you_want_exit)
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Выйти") { dialog, which ->
                findNavController().navigate(R.id.action_pop_back)
            }
    }

    private fun renderCards(state: ScreenStateCards) {
        when (state) {
            is ScreenStateCards.Content -> {
                listData = state.cards
                val expandableListView = binding.cardsNlv
                val adapter = CardsExpandableListAdapter(requireContext(), resources.getString(R.string.cards), listData)
                expandableListView.setAdapter(adapter)
            }

            else -> {}
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            confirmDialog.show()
        }
    }
}
