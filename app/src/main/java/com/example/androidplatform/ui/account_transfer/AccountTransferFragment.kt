package com.example.androidplatform.ui.account_transfer

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidplatform.databinding.FragmentAccountTransferBinding
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.presentation.account_info.models.AccountInfoState
import com.example.androidplatform.presentation.account_transfer.models.AccountTransferScreenState
import com.example.androidplatform.presentation.account_transfer.viemodel.AccountTransferViewModel
import com.example.androidplatform.util.MinMaxFilter
import com.example.androidplatform.util.toCurrencyMoneyFormat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountTransferFragment : Fragment() {
    private var _binding: FragmentAccountTransferBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AccountTransferViewModel>()
    private val navArgs: AccountTransferFragmentArgs by navArgs()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val bottomSheetAdapter = BottomSheetAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextChangeListeners()

        viewModel.isButtonVisible.observe(viewLifecycleOwner) {
            binding.nextBtn.isVisible = it
        }

        binding.sumEt.filters = arrayOf<InputFilter>(MinMaxFilter(1, 1000000000))

        if (navArgs.accountId != 0) {
            viewModel.getAccount(navArgs.accountId)
            viewModel.accountScreenState().observe(viewLifecycleOwner) {
                renderRecipientAccount(it)
            }
        }

        binding.nextBtn.setOnClickListener {
            findNavController().navigate(
                AccountTransferFragmentDirections
                    .actionAccountTransferFragmentToConfirmTransferOperationFragment(
                        viewModel.writeOffAccount.value!!,
                        viewModel.recipientAccount.value!!,
                        viewModel.sum.value!!,
                        navArgs.accountId
                    )
            )
        }

        val bottomSheetContainer = binding.bottomSheetLl
        val overlay = binding.overlay
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.accountsListRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.accountsListRv.adapter = bottomSheetAdapter

        viewModel.observeMode().observe(viewLifecycleOwner) {
            renderMode(it)
        }

        binding.writeOffAccountLl.setOnClickListener {
            bottomSheetAdapter.apply {
                accountClickListener = BottomSheetAdapter.WriteOffAccountClickListener { account ->
                    updateWriteOffAccount(account)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            viewModel.onWriteOffAccountChooseClick()
        }

        binding.recipientAccountLl.setOnClickListener {
            bottomSheetAdapter.apply {
                accountClickListener = BottomSheetAdapter.RecipientAccountClickListener { account ->
                    updateRecipientAccount(account)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            viewModel.onRecipientAccountChooseClick()
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun renderMode(mode: AccountTransferScreenState) {
        binding.overlay.isVisible = mode is AccountTransferScreenState.BottomSheet
        binding.bottomSheetLl.isVisible = mode is AccountTransferScreenState.BottomSheet
        if (mode is AccountTransferScreenState.BottomSheet) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetAdapter.addItems(mode.accounts)
        }
    }

    private fun renderRecipientAccount(state: AccountInfoState) {
        when (state) {
            is AccountInfoState.Content -> updateRecipientAccount(state.account)
            is AccountInfoState.Error -> {}
        }
    }

    private fun updateRecipientAccount(account: Account) = with(binding) {
        viewModel.updateRecipientSelectedAccount(account)
        recipientAccountTv.visibility = View.VISIBLE
        recipientAccountHintTv.visibility = View.GONE
        recipientAccountIv.visibility = View.VISIBLE
        recipientAccountInfoLl.visibility = View.VISIBLE
        recipientAccountNumberTv.text = account.number
        recipientBalanceTv.text = account.balance.toString().toCurrencyMoneyFormat(account.currency)
    }


    private fun updateWriteOffAccount(account: Account) = with(binding) {
        viewModel.updateWriteOffSelectedAccount(account)
        writeOffAccountTv.visibility = View.VISIBLE
        writeOffAccountHintTv.visibility = View.GONE
        writeOffAccountIv.visibility = View.VISIBLE
        writeOffAccountInfoLl.visibility = View.VISIBLE
        writeOffAccountNumberTv.text = account.number
        writeOffAccountBalanceTv.text =
            account.balance.toString().toCurrencyMoneyFormat(account.currency)
    }

    private fun addTextChangeListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.apply {
                    addWriteOffAccount(binding.writeOffAccountNumberTv.text.toString())
                    addRecipientAccount(binding.recipientAccountNumberTv.text.toString())
                    addSum(binding.sumEt.text.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        binding.apply {
            writeOffAccountNumberTv.addTextChangedListener(textWatcher)
            recipientAccountNumberTv.addTextChangedListener(textWatcher)
            sumEt.addTextChangedListener(textWatcher)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
