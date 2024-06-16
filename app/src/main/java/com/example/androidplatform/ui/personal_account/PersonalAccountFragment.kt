package com.example.androidplatform.ui.personal_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidplatform.databinding.FragmentPersonalAccountBinding
import com.example.androidplatform.domain.models.clients.ClientsItem
import com.example.androidplatform.presentation.personal_account.models.ScreenStateClients
import com.example.androidplatform.presentation.personal_account.viewmodel.PersonalAccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        viewModel.getClients("token") // через модуль авторизации получить токен

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: ScreenStateClients) {
        when (state) {
            is ScreenStateClients.Content -> {
                showContent(state.listClients)
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

    private fun showContent(clientsList: List<ClientsItem>) {
        binding.fullNameTv.text = clientsList[0].apply {
            "$lastName $firstName $middleName"
        }.toString()

    }
}