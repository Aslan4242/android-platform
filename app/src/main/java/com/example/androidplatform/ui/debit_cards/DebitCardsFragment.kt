package com.example.androidplatform.ui.debit_cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentDebitCardsBinding

class DebitCardsFragment : Fragment() {
    private var _binding: FragmentDebitCardsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebitCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.orderDebitCardBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("cardProduct", "DEBIT_CARD")
            }
            findNavController().navigate(R.id.action_cards_fragment_to_personal_data_by_card_ordering_fragment, bundle)
        }
    }

    companion object {
        fun newInstance() = DebitCardsFragment()
    }
}