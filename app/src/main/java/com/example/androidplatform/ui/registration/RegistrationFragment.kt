package com.example.androidplatform.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidplatform.databinding.FragmentAuthorizationBinding
import com.example.androidplatform.databinding.FragmentRegiststrationBinding
import com.example.androidplatform.databinding.FragmentRestorePasswordBinding

class RegistrationFragment : Fragment()  {
    private var _binding: FragmentRegiststrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegiststrationBinding.inflate(inflater, container, false)
        return binding.root
    }
}