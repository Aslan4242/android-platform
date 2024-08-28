package com.example.androidplatform.ui.account_replenishment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentAccountReplenishmentBinding

class AccountReplenishmentFragment : Fragment() {
    private var _binding: FragmentAccountReplenishmentBinding? = null
    private val binding get() = _binding!!
    private val navArgs: AccountReplenishmentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountReplenishmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.accountTransferBtn.setOnClickListener {
            if (navArgs.accountId == 0) {
                findNavController().navigate(R.id.action_accountReplenishmentFragment_to_accountTransferFragment)
            } else {
                findNavController().navigate(
                    AccountReplenishmentFragmentDirections
                        .actionAccountReplenishmentFragmentToAccountTransferFragment(navArgs.accountId)
                )
            }
        }

        binding.accountRefillBtn.setOnClickListener {
            if (navArgs.accountId == 0) {
                findNavController().navigate(R.id.action_accountReplenishmentFragment_to_accountRefillFragment)
            } else {
                findNavController().navigate(
                    AccountReplenishmentFragmentDirections
                        .actionAccountReplenishmentFragmentToAccountRefillFragment(navArgs.accountId)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
