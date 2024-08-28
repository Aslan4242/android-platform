package com.example.androidplatform.ui.account_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentAccountInfoBinding
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.domain.models.account.AccountState
import com.example.androidplatform.presentation.account_info.models.AccountInfoState
import com.example.androidplatform.presentation.account_info.models.AccountLockState
import com.example.androidplatform.presentation.account_info.viewmodel.AccountInfoViewModel
import com.example.androidplatform.util.toCurrencyMoneyFormat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountInfoFragment : Fragment() {
    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AccountInfoViewModel>()
    private val navArgs: AccountInfoFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState().observe(viewLifecycleOwner) {
            renderAccountInfo(it)
        }
        viewModel.lockState().observe(viewLifecycleOwner) {
            renderLockState(it)
        }

        viewModel.getAccount(navArgs.accountId)

        binding.accountReplenishmentBtn.setOnClickListener {
            findNavController().navigate(
                AccountInfoFragmentDirections
                    .actionAccountInfoFragmentToAccountReplenishmentFragment(navArgs.accountId)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderAccountInfo(state: AccountInfoState) {
        when (state) {
            is AccountInfoState.Content -> showAccountInfo(state.account)
            is AccountInfoState.Error -> {}
        }
    }

    private fun renderLockState(state: AccountLockState) {
        when (state) {
            is AccountLockState.Content -> refreshData()
            is AccountLockState.Error -> {}
        }
    }

    private fun showAccountInfo(account: Account) = with(binding) {
        cardSrl.setOnRefreshListener {
            refreshData()
            cardSrl.isRefreshing = false
        }
        accountInfo.text = account.name
        accountBalanceValueTv.text = account.balance.toString()
            .toCurrencyMoneyFormat(account.currency)
        when (account.state) {
            AccountState.ACTIVE.value -> with(blockAccountButton) {
                setText(R.string.block_account)
                setOnClickListener {
                    showDialog(
                        R.string.block_account,
                        R.string.dialog_block_account_message,
                        true,
                        account.id
                    )
                }
            }

            AccountState.BLOCKED.value -> with(blockAccountButton) {
                setText(R.string.unblock_account)
                setOnClickListener {
                    showDialog(
                        R.string.unblock_account,
                        R.string.dialog_unblock_account_message,
                        false,
                        account.id
                    )
                }
            }

            else -> blockAccountButton.visibility = View.GONE
        }
        accountNumberValueTv.text = account.number
        accountStateValueTv.text = getCardState(account.state)
    }

    private fun refreshData() {
        with(viewModel) {
            getAccount(navArgs.accountId)
        }
    }

    private fun getCardState(cardState: String): String = with(resources) {
        return when (cardState) {
            AccountState.ACTIVE.value -> getString(R.string.account_state_active)
            AccountState.BLOCKED.value -> getString(R.string.account_state_blocked)
            else -> ""
        }
    }

    private fun showDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        lock: Boolean,
        accountId: Int
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(R.string.no) { dialog, which ->
                // ничего не делаем
            }.setNegativeButton(R.string.yes) { dialog, which ->
                if (lock) viewModel.blockAccountById(accountId) else viewModel.unlockAccountById(
                    accountId
                )
            }.show()
    }
}
