package com.example.androidplatform.ui.change_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentChangePasswordBinding
import com.example.androidplatform.presentation.change_password.ChangePasswordViewModel
import com.example.androidplatform.presentation.change_password.models.ChangePasswordState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChangePasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val login = arguments?.get("login") as? String
        viewModel.setLogin(login)
        observeViewModel()
        setClickListeners()
        addTextChangeListeners()
    }

    private fun setClickListeners() {
        binding.enterBtn.setOnClickListener {
            val password1 = binding.etNewPassword.text?.toString()
            val password2 = binding.repeatPasswordEt.text?.toString()
            viewModel.changePassword(password1, password2)
        }
    }

    private fun observeViewModel() {
        viewModel.errorInputPassword1.observe(viewLifecycleOwner) {
            val errorMessage = if (it) getString(R.string.send_password_error) else null
            binding.tilNewPassword.error = errorMessage
        }
        viewModel.errorInputPassword2.observe(viewLifecycleOwner) {
            val errorMessage = if (it) getString(R.string.send_password_error) else null
            binding.tilRepeatPassword.error = errorMessage
        }
        viewModel.isButtonEnabled.observe(viewLifecycleOwner) {
            binding.enterBtn.isEnabled = it
        }
        viewModel.screenState.observe(viewLifecycleOwner) {
            when(it) {
                is ChangePasswordState.Loading -> {
                    binding.progressBar.visibility = VISIBLE
                }
                is ChangePasswordState.Content -> {
                    binding.progressBar.visibility = GONE
                    showToast(getString(R.string.password_change_successful))
                    findNavController().popBackStack()
                }
                is ChangePasswordState.ContentAuth -> {

                }
                is ChangePasswordState.Error -> {
                    binding.progressBar.visibility = GONE
                    showToast(it.message.toString())
                }
            }
        }
        viewModel.showToastMessage.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun addTextChangeListeners() {
        binding.etNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.changePassword1(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.repeatPasswordEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.changePassword2(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}