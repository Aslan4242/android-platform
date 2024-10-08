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
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentUpdateUserBinding
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.update_user.UpdateUserViewModel
import com.example.androidplatform.presentation.update_user.model.UpdateState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class UpdateUserFragment : Fragment() {
    private var _binding: FragmentUpdateUserBinding? = null
    private val binding: FragmentUpdateUserBinding get() = _binding!!
    private val viewModel: UpdateUserViewModel by viewModel<UpdateUserViewModel>()
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.do_you_want_exit)
            .setMessage(R.string.unsaved_changes)
            .setNeutralButton(R.string.cancel) { _, _ ->
                // ничего не делаем
            }.setNegativeButton(R.string.logout) { _, _ ->
                findNavController().popBackStack(R.id.updateUserFragment, true)
            }

        observeViewModel()
        binding.updateBtn.setOnClickListener {
            binding.updateBtn.apply {
                isEnabled = false
                setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.gray_2)
                )
            }
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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    private fun observeViewModel() {
        viewModel.isButtonEnabled.observe(viewLifecycleOwner) {
            binding.updateBtn.apply {
                isEnabled = it
                if (it)
                    setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.orange)
                    )
                else
                    setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.gray_2)
                    )
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
            when (it) {
                is UpdateState.Content -> {
                    showToast("Данные пользователя успешно обновлены")
                }

                is UpdateState.Error -> {
                    showToast("Произошла ошибка!")
                }
            }
            findNavController().popBackStack(R.id.updateUserFragment, true)
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
            loginEt.append(client.login)
            phoneNumberEt.append(client.phoneNumber)
            emailEt.append(client.email)
            lastnameEt.append(client.lastName)
            firstnameEt.append(client.firstName)
            middlenameEt.append(client.middleName)
            birthdateEt.setText(viewModel.getDateTime(client.birthdate))
            addressEt.append(client.address)
        }
        selectGender(client.sex)
    }

    private fun selectGender(sex: String) {
        when (sex) {
            requireContext().resources.getString(R.string.male_eng) ->
                binding.maleRadioButton.isChecked = true

            requireContext().resources.getString(R.string.female_eng) ->
                binding.femaleRadioButton.isChecked = true
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

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.isButtonEnabled.value == true) {
                confirmDialog.show()
            } else {
                findNavController().popBackStack(R.id.updateUserFragment, true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
