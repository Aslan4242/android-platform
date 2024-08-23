package com.example.androidplatform.ui.card_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentCardInfoBinding
import com.example.androidplatform.domain.models.cards.Card
import com.example.androidplatform.domain.models.cards.CardState
import com.example.androidplatform.presentation.card_info.models.CardCvcState
import com.example.androidplatform.presentation.card_info.models.CardInfoState
import com.example.androidplatform.presentation.card_info.models.CardLockState
import com.example.androidplatform.presentation.card_info.viewmodel.CardInfoViewModel
import com.example.androidplatform.util.toCurrencyMoneyFormat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardInfoFragment : Fragment() {
    private var _binding: FragmentCardInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CardInfoViewModel>()
    private val navArgs: CardInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState().observe(viewLifecycleOwner) {
            renderCardInfo(it)
        }
        viewModel.cvcState().observe(viewLifecycleOwner) {
            renderCvc(it)
        }
        viewModel.lockState().observe(viewLifecycleOwner) {
            renderLockState(it)
        }

        viewModel.getCard(navArgs.cardId)
        viewModel.getCardCvc(navArgs.cardId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderCardInfo(state: CardInfoState) {
        when (state) {
            is CardInfoState.Content -> showCardInfo(state.card)
            is CardInfoState.Error -> {}
        }
    }

    private fun renderCvc(state: CardCvcState) {
        when (state) {
            is CardCvcState.Content -> setupCvc(state.cardCvc)
            is CardCvcState.Error -> {}
        }
    }

    private fun renderLockState(state: CardLockState) {
        when (state) {
            is CardLockState.Content -> refreshData()
            is CardLockState.Error -> {}
        }
    }

    private fun showCardInfo(card: Card) = with (binding) {
        cardSrl.setOnRefreshListener {
            refreshData()
            cardSrl.isRefreshing = false
        }
        cardName.text = card.account.name
        when(card.state) {
            CardState.CREATED.value,
            CardState.ACTIVE.value -> with (blockCardButton) {
                setText(R.string.block_card)
                setOnClickListener { showDialog(
                    R.string.dialog_block_card_title,
                    R.string.dialog_block_card_message,
                    true,
                    card.id
                ) }
            }
            CardState.LOCKED.value -> with (blockCardButton) {
                setText(R.string.unblock_card)
                setOnClickListener { showDialog(
                    R.string.dialog_unblock_card_title,
                    R.string.dialog_unblock_card_message,
                    false,
                    card.id
                ) }
            }
            else -> blockCardButton.visibility = View.GONE
        }
        cardNumberValue.text = getString(R.string.card_number_hiden)
            .format(card.number.substring(card.number.length - 4))
        cardExpirationMonthValue.text = card.month
        cardExpirationYearValue.text = card.year

        cardProgramIcon.setImageResource(getCardImage(card.cardProgram))
        cardBalanceValue.text = card.account.balance.toString()
            .toCurrencyMoneyFormat(card.account.currency)
        cardProductNameValue.text = card.product.name
        cardStateValue.text = getCardState(card.state)
    }

    private fun setupCvc(cardCvc: Int) = with (binding) {
        cardCvcVisibleIcon.setOnClickListener { view ->
            if (view.isSelected) {
                view.isSelected = false
                cardCvcValue.setText(R.string.cvc_code_hiden)
            } else {
                view.isSelected = true
                cardCvcValue.text = cardCvc.toString()
            }
        }
    }

    private fun refreshData() {
        with (viewModel) {
            getCard(navArgs.cardId)
            getCardCvc(navArgs.cardId)
        }
    }

    private fun getCardImage(cardProgram: String): Int = with(resources) {
        return when (cardProgram) {
            getString(R.string.mir) -> R.drawable.mir
            getString(R.string.visa) -> R.drawable.visa
            getString(R.string.maestro) -> R.drawable.maestro
            getString(R.string.mastercard) -> R.drawable.mastercard
            else -> 0
        }
    }

    private fun getCardState(cardState: String): String = with(resources) {
        return when (cardState) {
            CardState.CREATED.value -> getString(R.string.card_state_created)
            CardState.ACTIVE.value -> getString(R.string.card_state_active)
            CardState.LOCKED.value -> getString(R.string.card_state_locked)
            CardState.EXPIRED.value -> getString(R.string.card_state_expired)
            CardState.BLOCKED.value -> getString(R.string.card_state_blocked)
            else -> ""
        }
    }

    private fun showDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        lock: Boolean,
        cardId: Int
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(R.string.no) { dialog, which ->
                // ничего не делаем
            }.setNegativeButton(R.string.yes) { dialog, which ->
                if (lock) viewModel.blockCardById(cardId) else viewModel.unlockCardById(cardId)
            }.show()
    }
}
