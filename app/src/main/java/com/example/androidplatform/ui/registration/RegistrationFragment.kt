package com.example.androidplatform.ui.registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentRegiststrationBinding
import com.example.androidplatform.presentation.registration.RegistrationViewModel
import com.example.androidplatform.presentation.registration.models.RegistrationState
import com.example.androidplatform.ui.change_password.ChangePasswordFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegiststrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegiststrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var gender = "Unknown"

        binding.genderRadioGroup.setOnCheckedChangeListener { group, _ ->
            gender = getGender(group)
        }

        observeViewModel()
        addTextChangeListeners()
        setClickListeners(gender)

        binding.birthdateEt.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                R.style.DatePickerDialogTheme,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate =
                        String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.birthdateEt.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        listOf(
            binding.passwordTil,
            binding.passwordRepeatTil
        ).forEach { textInputLayout ->
            textInputLayout.helperText = getString(R.string.required_field) + ". " +
                    String.format(
                        getString(R.string.password_length_range),
                        MIN_LENGTH,
                        MAX_LENGTH
                    )
        }

    }

    private fun getGender(group: RadioGroup): String {
        return when (group.checkedRadioButtonId) {
            R.id.male_radio_button -> requireContext().resources.getString(R.string.male_eng)
            R.id.female_radio_button -> requireContext().resources.getString(R.string.female_eng)
            else -> {
                "Unknown"
            }
        }
    }

    private fun setClickListeners(gender: String) {
        binding.registerBtn.setOnClickListener {
            viewModel.registration(
                login = binding.loginEt.text.toString(),
                password = binding.passwordEt.text.toString(),
                repeatPassword = binding.passwordRepeatEt.text.toString(),
                phoneNumber = binding.phoneNumberEt.text.toString(),
                email = binding.emailEt.text.toString(),
                name = binding.nameEt.text.toString(),
                surname = binding.surnameEt.text.toString(),
                patronymic = binding.patronymicEt.text.toString(),
                birthdate = binding.birthdateEt.text.toString(),
                address = binding.addressEt.text.toString(),
                sex = gender
            )
        }
    }

    private fun observeViewModel() {
        viewModel.errorInputPassword1.observe(viewLifecycleOwner) {
            val errorMessage = if (it) getString(R.string.send_password_error) else null
            binding.passwordTil.error = errorMessage
        }
        viewModel.errorInputPassword2.observe(viewLifecycleOwner) {
            val errorMessage = if (it) getString(R.string.send_password_error) else null
            binding.passwordRepeatTil.error = errorMessage
        }
        viewModel.isButtonEnabled.observe(viewLifecycleOwner) {
            binding.registerBtn.apply {
                isEnabled = it
                if (it)
                    setBackgroundColor(resources.getColor(R.color.orange))
                else
                    setBackgroundColor(resources.getColor(R.color.gray_2))
            }
        }
        viewModel.screenState().observe(viewLifecycleOwner) {
            when (it) {
                is RegistrationState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is RegistrationState.Content -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(getString(R.string.successful_registration))
                    findNavController().popBackStack()
                }

                is RegistrationState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(context?.resources?.getString(it.message) ?: "")
                }
            }
        }
        viewModel.showToastMessage.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun addTextChangeListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.apply {
                    addLogin(binding.loginEt.text.toString())
                    addEmail(binding.emailEt.text.toString())
                    addBirthdate(binding.birthdateEt.text.toString())
                    addPassword1(binding.passwordEt.text.toString())
                    addPassword2(binding.passwordRepeatEt.text.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.apply {
            loginEt.addTextChangedListener(textWatcher)
            emailEt.addTextChangedListener(textWatcher)
            birthdateEt.addTextChangedListener(textWatcher)
            passwordEt.addTextChangedListener(textWatcher)
            passwordRepeatEt.addTextChangedListener(textWatcher)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val MIN_LENGTH = 8
        const val MAX_LENGTH = 29
    }
}