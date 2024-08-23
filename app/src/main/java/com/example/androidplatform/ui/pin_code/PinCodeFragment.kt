package com.example.androidplatform.ui.pin_code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.androidplatform.R
import com.example.androidplatform.data.network.cards.ActivateCardRequest
import com.example.androidplatform.databinding.FragmentPinCodeBinding
import com.example.androidplatform.presentation.pin_code.viewmodel.PinCodeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinCodeFragment : Fragment() {

    private var _binding: FragmentPinCodeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PinCodeViewModel>()
    private val navArgs: PinCodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPinCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.activationState().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding.submitPinCode.setOnClickListener {
            val text = binding.pinCode.text.toString()
            if (text.contains(Regex("\\d{4}"))) {
                viewModel.activateCardById(navArgs.cardId, ActivateCardRequest(pinCode = text.toInt()))
            } else {
                Toast.makeText(requireContext(), R.string.incorrect_pin_code, Toast.LENGTH_LONG).show()
            }
        }
    }
}
