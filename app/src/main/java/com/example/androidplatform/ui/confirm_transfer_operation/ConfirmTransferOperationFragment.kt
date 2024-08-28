package com.example.androidplatform.ui.confirm_transfer_operation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentConfirmTransferOperationBinding
import com.example.androidplatform.presentation.confirm_transfer_operation.viewmodel.ConfirmTransferOperationViewModel
import com.example.androidplatform.presentation.personal_data_by_card_ordering.models.OperationState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmTransferOperationFragment : Fragment() {
    private var _binding: FragmentConfirmTransferOperationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ConfirmTransferOperationViewModel>()
    private val navArgs: ConfirmTransferOperationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmTransferOperationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showContent(
            navArgs.writeOffAccountNumber,
            navArgs.recipientAccountNumber,
            navArgs.sum
        )

        binding.confirmBtn.setOnClickListener {
            viewModel.transfer(
                navArgs.writeOffAccountNumber,
                navArgs.recipientAccountNumber,
                navArgs.sum
            )
        }

        viewModel.showToastMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        viewModel.operationState().observe(viewLifecycleOwner) { state ->
            if (state is OperationState.Content) {
                if (navArgs.accountId == 0) {
                    findNavController().navigate(R.id.action_confirmTransferOperationFragment_to_dashboardFragment)
                } else {
                    findNavController().navigate(
                        ConfirmTransferOperationFragmentDirections
                            .actionConfirmTransferOperationFragmentToAccountInfoFragment(navArgs.accountId),
                        NavOptions.Builder().setPopUpTo(R.id.accountInfoFragment, true).build()
                    )
                }
                Toast.makeText(
                    requireContext(),
                    getString(R.string.operation_successfully_completed),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showContent(
        writeOffAccountNumber: String,
        recipientAccountNumber: String,
        sum: String
    ) {
        binding.writeOffAccountNumberTv.text = writeOffAccountNumber
        binding.recipientAccountNumberTv.text = recipientAccountNumber
        binding.sumTv.text = sum
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
