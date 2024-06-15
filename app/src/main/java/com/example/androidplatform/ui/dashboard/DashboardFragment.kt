package com.example.androidplatform.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidplatform.databinding.FragmentAuthorizationBinding
import com.example.androidplatform.databinding.FragmentDashboardBinding
import com.example.androidplatform.databinding.FragmentRegiststrationBinding
import com.example.androidplatform.databinding.FragmentRestorePasswordBinding

class DashboardFragment : Fragment()  {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }
}