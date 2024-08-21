package com.example.androidplatform.ui.current_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentCurrentAccountBinding

class CurrentAccountFragment : Fragment() {
    private var _binding: FragmentCurrentAccountBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openCurrentAccountBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("accountType", "CURRENT_ACCOUNT")
            }
            findNavController().navigate(R.id.action_accounts_fragment_to_personal_data_by_account_opening_fragment, bundle)
        }
    }

    companion object {
        fun newInstance() = CurrentAccountFragment()
    }
}
