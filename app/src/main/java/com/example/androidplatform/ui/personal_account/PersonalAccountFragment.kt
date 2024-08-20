package com.example.androidplatform.ui.personal_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentPersonalAccountBinding
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.presentation.logout.models.LogoutState
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.personal_account.viewmodel.PersonalAccountViewModel
import com.example.androidplatform.util.parseDate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.format.DateTimeFormatter

class PersonalAccountFragment : Fragment() {
    private var _binding: FragmentPersonalAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PersonalAccountViewModel>()
    private lateinit var confirmDialog: MaterialAlertDialogBuilder

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

        viewModel.logoutScreenState().observe(viewLifecycleOwner) {
            logoutMessage(it)
        }

        viewModel.showToastMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.do_you_want_exit)
            .setNeutralButton(R.string.cancel) { _, _ ->
                // ничего не делаем
            }.setNegativeButton(R.string.logout) { _, _ ->
                viewModel.logout()
            }

        binding.logoutBtn.setOnClickListener {
            confirmDialog.show()
        }

        binding.editingUserBtn.setOnClickListener {
            findNavController()
                .navigate(R.id.action_personalAccountFragment_to_updateUserFragment)
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

    private fun logoutMessage(state: LogoutState) {
        when (state) {
            is LogoutState.Content -> {
                findNavController().navigate(R.id.action_logout_pop_back)
                Toast.makeText(
                    requireContext(),
                    state.message,
                    Toast.LENGTH_LONG
                ).show()
            }

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
        binding.sexTv.text = translateGender(client.sex)
        binding.birthdateTv.text = convertDateTime(client.birthdate)
        binding.phoneNumberTv.text = client.phoneNumber
        binding.addressTv.text = client.address
    }

    private fun convertDateTime(input: String): String {
        val dateTime = input.parseDate()
        val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return dateTime.format(outputFormatter)
    }

    private fun translateGender(gender: String): String {
        return when (gender) {
            requireContext().getString(R.string.male_eng) -> requireContext().getString(R.string.male)
            requireContext().getString(R.string.female_eng) -> requireContext().getString(R.string.female)
            else -> "Не указан"
        }
    }
}
