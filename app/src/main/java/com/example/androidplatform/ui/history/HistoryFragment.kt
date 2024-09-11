package com.example.androidplatform.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentHistoryBinding
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.domain.models.clients.Client
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

        adapter = HistoryListAdapter { transactionId ->
            findNavController().navigate(HistoryFragmentDirections
                .actionHistoryFragmentToTransactionInfoFragment(transactionId))
        }
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
        val transactionHistoryList = historyList.ifEmpty {
            listOf(
                Transaction(
                    id = 0,
                    account = Account(
                        id = 0,
                        client = null,
                        createdDate = "2024-08-18T12:23:07.757386",
                        currency = 643,
                        number = "40861156574182180337",
                        name = "Текущий счёт",
                        balance = 40000,
                        state = "Active"
                    ),
                    receiver = "40835111716751865322",
                    date = "2024-08-26T19:56:56.017938",
                    paymentDate = "2024-08-26T19:56:56.017938",
                    amount = 13000,
                    comment = "Платеж за макдак",
                    reason = null,
                    state = "Completed",
                    type = "Expense"
                )
            )
        }

        with(binding) {
            historySrl.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
        val expenseBlockData = HistoryExpenseBlockData(
            header = R.string.spending,
            text = viewModel.getTransactionSum(transactionHistoryList)
        )
        adapter.submitList(
            mutableListOf<Any>(expenseBlockData).apply {
                addAll(viewModel.groupTransactionsByDate(transactionHistoryList))
            }
        )
    }

    private fun showLoading() {
        with(binding) {
            historySrl.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError(@StringRes message: Int) {
        with(binding) {
            historySrl.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
