package com.example.androidplatform.ui.authorization

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
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

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateEnterButton()
            }
        }

        binding.mobileEt.addTextChangedListener(textWatcher)
        binding.passwordEt.addTextChangedListener(textWatcher)

        binding.enterBtn.setOnClickListener {
            if (binding.mobileEt.text.isNotEmpty() && binding.passwordEt.text.isNotEmpty()) {
                viewModel.authenticate(
                    login = binding.mobileEt.text.toString(),
                    password = binding.passwordEt.text.toString()
                )
            }
        }

        binding.registrationTv.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }

        binding.restorePasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_restorePasswordFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
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
        if (binding.mobileEt.text.isNotEmpty() && binding.passwordEt.text.isNotEmpty()) {
            binding.enterBtn.apply {
                isEnabled = true
                setBackgroundColor(resources.getColor(R.color.orange))
            }
        } else {
            binding.enterBtn.apply {
                isEnabled = false
                setBackgroundColor(resources.getColor(R.color.gray_2))
            }
        }
    }
}
