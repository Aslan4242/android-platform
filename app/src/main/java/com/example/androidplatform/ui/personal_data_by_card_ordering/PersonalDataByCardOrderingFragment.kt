package com.example.androidplatform.ui.personal_data_by_card_ordering

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
import com.example.androidplatform.data.network.proceed_operation.card_ordering.CardProduct
import com.example.androidplatform.databinding.FragmentPersonalDataByCardOrderingBinding
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.personal_data_by_card_ordering.models.OperationState
import com.example.androidplatform.presentation.personal_data_by_card_ordering.viewmodel.PersonalDataByCardOrderingViewModel
import com.example.androidplatform.ui.root.RootActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalDataByCardOrderingFragment : Fragment() {
    private var _binding: FragmentPersonalDataByCardOrderingBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PersonalDataByCardOrderingViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var programType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalDataByCardOrderingBinding.inflate(inflater, container, false)
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
                binding.cardOrderedSuccessfullyLl.visibility = View.VISIBLE
            }
        }
    }

    private fun setupUI() {
        setupBottomSheet()
        setupButtonListeners()
        setupProgramTypeSelection()
        binding.programTypeEt.addTextChangedListener(createTextWatcher())
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
            findNavController().navigate(R.id.action_personalDataByCardOrderingFragment_to_dashBoardFragment)
        }

        binding.orderCardBtn.setOnClickListener {
            when (arguments?.getString("cardProduct")) {
                "DEBIT_CARD" -> viewModel.orderCard(
                    CardProduct.DEBIT_CARD,
                    viewModel.getPaymentType(programType)
                )

                "CREDIT_CARD" -> viewModel.orderCard(
                    CardProduct.CREDIT_CARD,
                    viewModel.getPaymentType(programType)
                )
            }
        }
    }

    private fun setupProgramTypeSelection() {
        binding.programTypeEt.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val programTypeMap = mapOf(
            binding.mirCardLl to R.string.mir,
            binding.visaCardLl to R.string.visa,
            binding.mastercardCardLl to R.string.mastercard,
            binding.maestroCardLl to R.string.maestro
        )
        programTypeMap.forEach { (view, stringResId) ->
            view.setOnClickListener {
                binding.programTypeEt.setText(stringResId)
                programType = getString(stringResId)
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
        with(binding.orderCardBtn) {
            isEnabled = binding.programTypeEt.text?.isNotEmpty() == true
            setBackgroundColor(resources.getColor(if (isEnabled) R.color.orange else R.color.gray_2))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

