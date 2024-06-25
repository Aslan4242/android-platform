package com.example.androidplatform.di

import android.app.Application
import com.example.androidplatform.presentation.authentication.viewmodel.AuthenticationViewModel
import com.example.androidplatform.presentation.change_password.ChangePasswordViewModel
import com.example.androidplatform.presentation.dashboard.viewmodel.DashBoardViewModel
import com.example.androidplatform.presentation.personal_account.viewmodel.PersonalAccountViewModel
import com.example.androidplatform.presentation.registration.RegistrationViewModel
import com.example.androidplatform.presentation.restoration_password.RestorePasswordViewModel
import org.koin.android.ext.koin.androidContext
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
                clientInteractor = get(),
                logoutInteractor = get()
            )
    }

    viewModel {
        DashBoardViewModel()
    }

    viewModel {
        RestorePasswordViewModel(
            restorePasswordInteractor = get(),
            application = androidContext() as Application
        )
    }

    viewModel {
        ChangePasswordViewModel(
            changePasswordInteractor = get(),
            authenticationInteractor = get(),
            application = androidContext() as Application
        )
    }
}
