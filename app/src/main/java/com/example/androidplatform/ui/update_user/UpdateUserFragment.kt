package com.example.androidplatform.ui.update_user

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
import com.example.androidplatform.databinding.FragmentUpdateUserBinding
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.update_user.UpdateUserViewModel
import com.example.androidplatform.presentation.update_user.model.UpdateState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class UpdateUserFragment : Fragment() {
    private var _binding: FragmentUpdateUserBinding? = null
    private val binding: FragmentUpdateUserBinding get() = _binding!!
    private val viewModel: UpdateUserViewModel by viewModel<UpdateUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        binding.registerBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.updateUser(collectData())
        }

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
        viewModel.getClients()
    }

    private fun observeViewModel() {
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
                is ScreenStateClients.Content -> {
                    binding.progressBar.visibility = View.GONE
                    showContent(it.client)
                    addTextChangeListeners()
                }

                is ScreenStateClients.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(context?.resources?.getString(it.message) ?: "")
                }

                else -> {}
            }
        }
        viewModel.updateUserState.observe(viewLifecycleOwner) {
            binding.registerBtn.apply {
                isEnabled = false
                setBackgroundColor(resources.getColor(R.color.gray_2))
            }
            when (it) {
                is UpdateState.Content -> {
                    showToast("Данные пользователя успешно обновлены")
                }

                is UpdateState.Error -> {
                    showToast("Произошла ошибка!")
                }
            }
            findNavController()
                .navigate(R.id.action_updateUserFragment_to_personalAccountFragment)
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
                val tempClient = collectData()
                viewModel.checkEnteredText(tempClient)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.apply {
            loginEt.addTextChangedListener(textWatcher)
            phoneNumberEt.addTextChangedListener(textWatcher)
            emailEt.addTextChangedListener(textWatcher)
            firstnameEt.addTextChangedListener(textWatcher)
            lastnameEt.addTextChangedListener(textWatcher)
            middlenameEt.addTextChangedListener(textWatcher)
            birthdateEt.addTextChangedListener(textWatcher)
            addressEt.addTextChangedListener(textWatcher)
            genderRadioGroup.setOnCheckedChangeListener { _, _ ->
                val tempClient = collectData()
                viewModel.checkEnteredText(tempClient)
            }
        }

    }

    private fun showContent(client: Client) {
        with(binding) {
            loginEt.setText(client.login)
            phoneNumberEt.setText(client.phoneNumber)
            emailEt.setText(client.email)
            lastnameEt.setText(client.lastName)
            firstnameEt.setText(client.firstName)
            middlenameEt.setText(client.middleName)
            birthdateEt.setText(viewModel.getDateTime(client.birthdate))
            addressEt.setText(client.address)
        }
        selectGender(client.sex)

    }

    private fun selectGender(sex: String) {
        when (sex) {
            requireContext().resources.getString(R.string.male_eng) ->
                binding.maleRadioButton.isSelected = true

            requireContext().resources.getString(R.string.female_eng) ->
                binding.femaleRadioButton.isSelected = true
        }
    }

    private fun getGender(group: RadioGroup): String {
        return when (group.checkedRadioButtonId) {
            R.id.male_radio_button -> requireContext().resources.getString(R.string.male_eng)
            R.id.female_radio_button -> requireContext().resources.getString(R.string.female_eng)
            else -> {
                "Не указан"
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun collectData(): Client {
        with(binding) {
            return Client(
                login = loginEt.text.toString(),
                phoneNumber = phoneNumberEt.text.toString(),
                email = emailEt.text.toString(),
                firstName = firstnameEt.text.toString(),
                lastName = lastnameEt.text.toString(),
                middleName = middlenameEt.text.toString(),
                birthdate = viewModel.getServerDateTime(birthdateEt.text.toString()),
                address = addressEt.text.toString(),
                sex = getGender(genderRadioGroup),
                isMustChangePassword = false
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}