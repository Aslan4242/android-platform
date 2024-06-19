package com.example.androidplatform.ui.authorization

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentAuthorizationBinding
import com.example.androidplatform.presentation.authentication.models.StateAuthentication
import com.example.androidplatform.presentation.authentication.viewmodel.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorizationFragment : Fragment() {
    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AuthenticationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window = requireActivity().window
        window.navigationBarColor = resources.getColor(R.color.white)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (binding.loginEt.text.isNotEmpty()) {
                    binding.loginEt.setBackgroundResource(R.drawable.edit_text_frame)
                    binding.loginEt.setHintTextColor(resources.getColor(R.color.gray_3))
                }
                if (binding.passwordEt.text.isNotEmpty()) {
                    binding.passwordEt.setBackgroundResource(R.drawable.edit_text_frame)
                    binding.passwordEt.setHintTextColor(resources.getColor(R.color.gray_3))
                }
                updateEnterButton()
            }
        }

        binding.loginEt.addTextChangedListener(textWatcher)
        binding.passwordEt.addTextChangedListener(textWatcher)

        binding.enterBtn.setOnClickListener {
            if (binding.loginEt.text.isNotEmpty() && binding.passwordEt.text.isNotEmpty()) {
                viewModel.authenticate(
                    login = binding.loginEt.text.toString(),
                    password = binding.passwordEt.text.toString()
                )
            } else {
                showErrorMessage()
            }
        }

        binding.registrationTv.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }

        binding.restorePasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_restorePasswordFragment)
        }

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.showToastMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), resources.getString(it), Toast.LENGTH_LONG).show()
        }
    }

    private fun render(state: StateAuthentication) {
        when (state) {
            is StateAuthentication.Content -> {
                findNavController().navigate(R.id.action_authorizationFragment_to_dashboardFragment)
            }
            else -> {}
        }
    }

    private fun updateEnterButton() {
        if (binding.loginEt.text.isNotEmpty() && binding.passwordEt.text.isNotEmpty()) {
            binding.enterBtn.apply {
                setBackgroundColor(resources.getColor(R.color.orange))
            }
        } else {
            binding.enterBtn.apply {
                setBackgroundColor(resources.getColor(R.color.gray_2))
            }
        }
    }

    private fun showErrorMessage () {
        if (binding.loginEt.text.isEmpty()) {
            binding.loginEt.setBackgroundResource(R.drawable.edit_text_frame_error)
            binding.loginEt.setHintTextColor(resources.getColor(R.color.red_2))
        }
        if (binding.passwordEt.text.isEmpty()) {
            binding.passwordEt.setBackgroundResource(R.drawable.edit_text_frame_error)
            binding.passwordEt.setHintTextColor(resources.getColor(R.color.red_2))
        }
    }
}
