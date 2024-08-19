package com.example.androidplatform.ui.saving_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentSavingAccountBinding

class SavingAccountFragment : Fragment() {
    private var _binding: FragmentSavingAccountBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavingAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openSavingAccountBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("accountType", "SAVING_ACCOUNT")
            }
            findNavController().navigate(R.id.action_accounts_fragment_to_personal_data_by_account_opening_fragment, bundle)
        }
    }

    companion object {
        fun newInstance() = SavingAccountFragment()
    }
}
