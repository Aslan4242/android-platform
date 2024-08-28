package com.example.androidplatform.ui.personal_data_by_account_opening

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.data.network.proceed_operation.account_opening.AccountType
import com.example.androidplatform.databinding.FragmentPersonalDataByAccountOpeningBinding
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.personal_data_by_account_opening.viemodel.PersonalDataByAccountOpeningViewModel
import com.example.androidplatform.presentation.personal_data_by_card_ordering.models.OperationState
import com.example.androidplatform.ui.root.RootActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalDataByAccountOpeningFragment : Fragment() {
    private var _binding: FragmentPersonalDataByAccountOpeningBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PersonalDataByAccountOpeningViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var currencyType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalDataByAccountOpeningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObservers()
        setupUI()
    }

    private fun setupViewModelObservers() {
        viewModel.getClients()
        viewModel.screenState().observe(viewLifecycleOwner) { state ->
            if (state is ScreenStateClients.Content) {
                showClientData(state.client)
            }
        }
        viewModel.operationState().observe(viewLifecycleOwner) { state ->
            if (state is OperationState.Content) {
                binding.accountOpenedSuccessfullyLl.visibility = View.VISIBLE
            }
        }
    }

    private fun setupUI() {
        setupBottomSheet()
        setupButtonListeners()
        setupProgramTypeSelection()
        binding.accountCurrencyEt.addTextChangedListener(createTextWatcher())
    }

    private fun setupBottomSheet() {
        val bottomSheetContainer = binding.bottomSheetLl
        val overlay = binding.overlay
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(getBottomSheetCallback())
    }

    private fun setupButtonListeners() {
        binding.backToDashboardBtn.setOnClickListener {
            findNavController().navigate(R.id.action_personalDataByAccountOpeningFragment_to_dashBoardFragment)
        }

        binding.openAccountBtn.setOnClickListener {
            when (arguments?.getString("accountType")) {
                "SAVING_ACCOUNT" -> viewModel.openAccount(
                    AccountType.SAVING_ACCOUNT,
                    currencyType
                )

                "CURRENT_ACCOUNT" -> viewModel.openAccount(
                    AccountType.CURRENT_ACCOUNT,
                    currencyType
                )
            }
        }
    }

    private fun setupProgramTypeSelection() {
        binding.accountCurrencyEt.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val currencyTypeMap = mapOf(
            binding.rubleLl to R.string.ruble,
            binding.dollarLl to R.string.dollar,
            binding.euroLl to R.string.euro,
            binding.yuanLl to R.string.yuan
        )
        currencyTypeMap.forEach { (view, stringResId) ->
            view.setOnClickListener {
                binding.accountCurrencyEt.setText(stringResId)
                currencyType = getString(stringResId)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun getBottomSheetCallback() = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            binding.overlay.visibility =
                if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    private fun showClientData(client: Client) {
        binding.apply {
            surnameEt.setText(client.lastName)
            nameEt.setText(client.firstName)
            patronymicEt.setText(client.middleName)
            birthdateEt.setText(viewModel.convertDateTime(client.birthdate))
            genderRadioGroup.check(viewModel.getGenderId(client.sex))
            addressEt.setText(client.address)
        }
    }

    private fun createTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {
            updateOrderButton()
        }
    }

    private fun updateOrderButton() {
        with(binding.openAccountBtn) {
            isEnabled = binding.accountCurrencyEt.text?.isNotEmpty() == true
            setBackgroundColor(resources.getColor(if (isEnabled) R.color.orange else R.color.gray_2))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

