package com.example.androidplatform.di

import com.example.androidplatform.presentation.authentication.viewmodel.AuthenticationViewModel
import com.example.androidplatform.presentation.dashboard.viewmodel.DashBoardViewModel
import com.example.androidplatform.presentation.personal_account.viewmodel.PersonalAccountViewModel
import com.example.androidplatform.presentation.registration.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AuthenticationViewModel(
            authenticationInteractor = get()
        )
    }

    viewModel {
        RegistrationViewModel(
            registrationInteractor = get()
        )
    }

    viewModel {
            PersonalAccountViewModel(
                clientInteractor = get()
            )
    }

    viewModel {
        DashBoardViewModel()
    }
}
