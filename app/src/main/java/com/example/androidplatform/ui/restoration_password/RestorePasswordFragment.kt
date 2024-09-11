package com.example.androidplatform.ui.restoration_password

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
import com.example.androidplatform.databinding.FragmentRestorePasswordBinding
import com.example.androidplatform.presentation.restoration_password.RestorePasswordViewModel
import com.example.androidplatform.presentation.restoration_password.RestorePasswordViewModel.Companion.DEFAULT_PASSWORD
import com.example.androidplatform.presentation.restoration_password.models.RestorePasswordState
import com.example.androidplatform.services.NotificationManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestorePasswordFragment : Fragment() {
    private var _binding: FragmentRestorePasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<RestorePasswordViewModel>()
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestorePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setClickListeners()
        addTextChangeListeners()
        createDialog()
    }

    private fun setClickListeners() {
        binding.enterBtn.setOnClickListener {
            val login = binding.etLogin.text?.toString()
            val code = binding.smsCodeEt.text?.toString()
            viewModel.sendRestoreCode(login, code)
        }
        binding.tvTimer.setOnClickListener {
            binding.smsCodeEt.setText("")
            viewModel.restartTimer()
        }
    }

    private fun observeViewModel() {
        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
        viewModel.isSendCode.observe(viewLifecycleOwner) {
            with(binding) {
                smsCodeTil.visibility = VISIBLE
                tvTimer.visibility = VISIBLE
            }
        }
        viewModel.isChangeCode.observe(viewLifecycleOwner) {
            if (it) {
                with(binding) {
                    smsCodeTil.visibility = GONE
                    tvTimer.visibility = GONE
                    smsCodeEt.setText("")
                }
            }
        }
        viewModel.errorInputLogin.observe(viewLifecycleOwner) {
            val errorMessage = if (it) getString(R.string.login_error) else null
            binding.tilLogin.error = errorMessage
        }
        viewModel.errorInputCode.observe(viewLifecycleOwner) {
            val errorMessage = if (it) getString(R.string.code_error) else null
            binding.smsCodeTil.error = errorMessage
        }
        viewModel.isButtonEnabled.observe(viewLifecycleOwner) {
            binding.enterBtn.isEnabled = it
        }
        viewModel.screenState.observe(viewLifecycleOwner) {
            when (it) {
                is RestorePasswordState.Loading -> {
                    binding.progressBar.visibility = VISIBLE
                }
                is RestorePasswordState.Content -> {
                    binding.progressBar.visibility = GONE
                    confirmDialog.show()
                }
                is RestorePasswordState.Error -> {
                    binding.progressBar.visibility = GONE
                }
            }
        }
        viewModel.showToastMessage.observe(viewLifecycleOwner) {
            showToast(it)
        }
        viewModel.showNotificationMessage.observe(viewLifecycleOwner) {
            showPush(it)
        }
        viewModel.showWaitSmsInfo.observe(viewLifecycleOwner) {
            val visibility = if(it) VISIBLE else GONE
            binding.ivWaitTimer.visibility = visibility
            binding.tvWaitTimer.visibility = visibility
        }
    }

    private fun addTextChangeListeners() {
        binding.etLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString() ?: ""
                viewModel.resetErrorInputLogin()
                viewModel.changeLogin(text)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.smsCodeEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCode()
                viewModel.changeCode(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun showPush(text: String) {
        NotificationManager.showNotification(
            getString(R.string.notification_code),
            text,
            requireActivity().application
        )
        binding.smsCodeEt.setText(text)
    }

    private fun createDialog() {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.do_you_want_change_password)
            .setNegativeButton(R.string.reset) { dialog, which ->
                dialog.dismiss()
                showToast(
                    String.format(
                        resources.getText(R.string.password_was_reset).toString(),
                        DEFAULT_PASSWORD
                    )
                )
                findNavController().popBackStack()
            }.setPositiveButton(R.string.change) { dialog, which ->
                dialog.dismiss()
                findNavController().navigate(
                    RestorePasswordFragmentDirections
                        .actionRestorePasswordFragmentToChangePasswordFragment(binding.etLogin.text?.toString())
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}