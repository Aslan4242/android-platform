package com.example.androidplatform.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentAuthorizationBinding

class AuthorizationFragment : Fragment()  {
    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

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

        binding.enterBtn.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_dashboardFragment)
        }

        binding.registrationTv.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }

        binding.restorePasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_restorePasswordFragment)
        }
    }
}