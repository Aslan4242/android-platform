package com.example.androidplatform.ui.personal_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidplatform.databinding.FragmentPersonalAccountBinding
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.personal_account.viewmodel.PersonalAccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class PersonalAccountFragment : Fragment() {
    private var _binding: FragmentPersonalAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PersonalAccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getClients()

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: ScreenStateClients) {
        when (state) {
            is ScreenStateClients.Content -> {
                showContent(state.client)
            }

//            is ScreenStateClients.Empty -> {
//                showEmptyState(state.message)
//            }
//
//            is ScreenStateClients.Error -> {
//                showServerErrorState(state.message)
//            }
//
//            is ScreenStateClients.IsLoading -> {
//                showLoadingState()
//            }
//
//            is ScreenStateClients.NoInternet -> {
//                showNoInternetState(state.message)
//            }

            else -> {}
        }
    }

    private fun showContent(client: Client) {
        binding.fullNameTv.text = client.run {
            "$lastName $firstName $middleName"
        }
        binding.loginTv.text = client.login
        binding.emailTv.text = client.email
        binding.surnameTv.text = client.lastName
        binding.nameTv.text = client.firstName
        binding.patronymicTv.text = client.middleName
        binding.sexTv.text = client.sex
        binding.birthdateTv.text = convertDateTime(client.birthdate)
        binding.phoneNumberTv.text = client.phoneNumber
        binding.addressTv.text = client.address
    }

    private fun convertDateTime(input: String): String {
        val dateTime = parseDate(input)
        val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return dateTime.format(outputFormatter)
    }

    private fun parseDate(input: String): LocalDateTime {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .optionalStart()
            .appendPattern("'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 3, true)
            .optionalEnd()
            .optionalEnd()
            .toFormatter()

        return LocalDateTime.parse(input, formatter)
    }
}