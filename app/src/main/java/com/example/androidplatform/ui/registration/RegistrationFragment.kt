package com.example.androidplatform.ui.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentRegiststrationBinding
import com.example.androidplatform.presentation.registration.RegistrationViewModel
import com.example.androidplatform.presentation.registration.models.RegistrationState
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        var gender = requireContext().resources.getString(R.string.male_eng)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateRegisterButton()
            }
        }

        binding.genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            gender = getGender(group)
        }

        binding.loginEt.addTextChangedListener(textWatcher)
        binding.passwordEt.addTextChangedListener(textWatcher)

        binding.registerBtn.setOnClickListener {
            if (
                binding.loginEt.text.isNotEmpty()
                && binding.passwordEt.text.isNotEmpty()
                && binding.emailEt.text.isNotEmpty()
            ) {
                viewModel.createClient(
                    login = binding.loginEt.text.toString(),
                    password = binding.passwordEt.text.toString(),
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

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: RegistrationState) {
        when (state) {
            is RegistrationState.Content -> {
                findNavController().popBackStack()
            }
            else -> {}
        }
    }

    private fun getGender(group: RadioGroup): String {
        return when (group.checkedRadioButtonId) {
            R.id.male_radio_button -> requireContext().resources.getString(R.string.male_eng)
            R.id.female_radio_button -> requireContext().resources.getString(R.string.female_eng)
            else -> { "Не указан" }
        }
    }


    private fun updateRegisterButton() {
        if (
            binding.loginEt.text.isNotEmpty()
            && binding.passwordEt.text.isNotEmpty()
            && binding.emailEt.text.isNotEmpty()
        ) {
            binding.registerBtn.apply {
                isEnabled = true
                setBackgroundColor(resources.getColor(R.color.orange))
            }
        } else {
            binding.registerBtn.apply {
                isEnabled = false
                setBackgroundColor(resources.getColor(R.color.gray_2))
            }
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}