package com.example.androidplatform.ui.personal_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidplatform.databinding.FragmentDashboardBinding
import com.example.androidplatform.databinding.FragmentHistoryBinding
import com.example.androidplatform.databinding.FragmentPersonalAccountBinding

class PersonalAccountFragment : Fragment()  {
    private var _binding: FragmentPersonalAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalAccountBinding.inflate(inflater, container, false)
        return binding.root
    }
}