package com.example.androidplatform.ui.transaction_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentTransactionInfoBinding
import com.example.androidplatform.domain.models.history.Transaction
import com.example.androidplatform.domain.models.history.TransactionState
import com.example.androidplatform.domain.models.history.TransactionType
import com.example.androidplatform.presentation.transaction_info.models.TransactionInfoState
import com.example.androidplatform.presentation.transaction_info.viewmodel.TransactionInfoViewModel
import com.example.androidplatform.util.parseDate
import com.example.androidplatform.util.toCurrencyMoneyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.qualifier

class TransactionInfoFragment : Fragment() {
    private var _binding: FragmentTransactionInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<TransactionInfoViewModel>()

    private val navArgs: TransactionInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTransactionInfo(navArgs.transactionId)

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: TransactionInfoState) {
        when (state) {
            is TransactionInfoState.Content -> showContent(state.transaction)
            is TransactionInfoState.Empty -> showError(state.message)
            is TransactionInfoState.Error -> showError(state.message)
            is TransactionInfoState.IsLoading -> showLoading()
            is TransactionInfoState.NoInternet -> showError(state.message)
        }
    }

    private fun showContent(transaction: Transaction) {
        with(binding) {
            transactionInfoSv.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            accountNameValue.text = transaction.account.name
            if (transaction.receiver == null) {
                accountNumberLabel.visibility = View.GONE
                accountNumberValue.visibility = View.GONE
                accountNumberReceiverValue.text = transaction.account.number
            } else {
                if (transaction.type == TransactionType.EXPENSE.value) {
                    accountNumberValue.text = transaction.account.number
                    accountNumberReceiverValue.text = transaction.receiver
                } else {
                    accountNumberValue.text = transaction.receiver
                    accountNumberReceiverValue.text = transaction.account.number
                }
            }
            transactionDateValue.text = viewModel.convertDateTime(transaction.paymentDate)
            transactionAmountValue.text = transaction.amount.toString()
                .toCurrencyMoneyFormat(transaction.account.currency)
            transactionCommentValue.text = transaction.comment
            transaction.reason?.let {
                transactionReasonValue.visibility = View.VISIBLE
                transactionReasonLabel.visibility = View.VISIBLE
                transactionReasonValue.text = it
            }
            transactionStateValue.setText(
                when (transaction.state) {
                    TransactionState.HOLD.value -> R.string.transaction_state_hold
                    TransactionState.COMPLETED.value -> R.string.transaction_state_completed
                    TransactionState.CANCELED.value -> R.string.transaction_state_canceled
                    else -> R.string.unknown
                }
            )
            transactionTypeValue.setText(
                when(transaction.type) {
                    TransactionType.EXPENSE.value -> R.string.transaction_type_expense
                    TransactionType.INCOME.value -> R.string.transaction_type_income
                    else -> R.string.unknown
                }
            )
        }
    }

    private fun showLoading() {
        with(binding) {
            transactionInfoSv.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError(@StringRes message: Int) {
        with(binding) {
            transactionInfoSv.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
