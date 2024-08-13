package com.example.androidplatform.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentHistoryBinding
import com.example.androidplatform.domain.models.history.Transaction
import com.example.androidplatform.presentation.history.adapter.HistoryListAdapter
import com.example.androidplatform.presentation.history.models.HistoryExpenseBlockData
import com.example.androidplatform.presentation.history.models.HistoryState
import com.example.androidplatform.presentation.history.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<HistoryViewModel>()
    private lateinit var adapter: HistoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryListAdapter()
        binding.rvHistory.adapter = adapter

        binding.historySrl.apply {
            setOnRefreshListener {
                viewModel.getHistory()
                isRefreshing = false
            }
        }

        viewModel.getHistory()

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.showToastMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    private fun render(state: HistoryState) {
        when (state) {
            is HistoryState.Content -> showContent(state.historyList)
            is HistoryState.Empty -> showError(state.message)
            is HistoryState.Error -> showError(state.message)
            is HistoryState.IsLoading -> showLoading()
            is HistoryState.NoInternet -> showError(state.message)
        }
    }

    private fun showContent(historyList: List<Transaction>) {
        binding.apply {
            historySrl.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
        val expenseBlockData = HistoryExpenseBlockData(
            header = R.string.spending,
            text = viewModel.getTransactionSum(historyList)
        )
        adapter.submitList(
            mutableListOf<Any>(expenseBlockData).apply {
                addAll(viewModel.groupTransactionsByDate(historyList))
            }
        )
    }

    private fun showLoading() {
        binding.apply {
            historySrl.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError(@StringRes message: Int) {
        binding.apply {
            historySrl.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
}