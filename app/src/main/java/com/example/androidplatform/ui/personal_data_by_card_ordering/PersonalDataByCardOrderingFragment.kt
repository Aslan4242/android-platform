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
import com.example.androidplatform.data.network.proceed_operation.CardProduct
import com.example.androidplatform.data.network.proceed_operation.CardProgramType
import com.example.androidplatform.databinding.FragmentPersonalDataByCardOrderingBinding
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.personal_data_by_card_ordering.models.OperationState
import com.example.androidplatform.presentation.personal_data_by_card_ordering.viewmodel.PersonalDataByCardOrderingViewModel
import com.example.androidplatform.ui.root.RootActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

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
                    getPaymentType(programType)
                )

                "CREDIT_CARD" -> viewModel.orderCard(
                    CardProduct.CREDIT_CARD,
                    getPaymentType(programType)
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
            val isHidden = newState == BottomSheetBehavior.STATE_HIDDEN
            binding.overlay.visibility = if (isHidden) View.GONE else View.VISIBLE
            (activity as? RootActivity)?.findViewById<View>(R.id.bottomNavigationView)?.visibility =
                if (isHidden) View.VISIBLE else View.GONE
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }


    private fun showClientData(client: Client) {
        binding.apply {
            surnameEt.setText(client.lastName)
            nameEt.setText(client.firstName)
            patronymicEt.setText(client.middleName)
            birthdateEt.setText(convertDateTime(client.birthdate))
            genderRadioGroup.check(getGenderId(client.sex))
            addressEt.setText(client.address)
        }
    }

    private fun convertDateTime(input: String): String {
        return parseDate(input).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    private fun parseDate(input: String): LocalDateTime {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .optionalStart()
            .appendPattern("'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 3, true)
            .optionalEnd()
            .optionalEnd()
            .toFormatter()
        return LocalDateTime.parse(input, formatter)
    }

    private fun getGenderId(gender: String): Int {
        return when (gender) {
            getString(R.string.male_eng) -> R.id.male_radio_button
            getString(R.string.female_eng) -> R.id.female_radio_button
            else -> 0
        }
    }

    private fun getPaymentType(paymentType: String): CardProgramType {
        return when (paymentType) {
            getString(R.string.mir) -> CardProgramType.MIR
            getString(R.string.visa) -> CardProgramType.VISA
            getString(R.string.maestro) -> CardProgramType.MAESTRO
            else -> CardProgramType.MASTERCARD
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
