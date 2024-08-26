package com.example.androidplatform.di

import android.app.Application
import com.example.androidplatform.presentation.authentication.viewmodel.AuthenticationViewModel
import com.example.androidplatform.presentation.card_info.viewmodel.CardInfoViewModel
import com.example.androidplatform.presentation.cards.viewmodel.CardsViewModel
import com.example.androidplatform.presentation.change_password.ChangePasswordViewModel
import com.example.androidplatform.presentation.dashboard.viewmodel.DashBoardViewModel
import com.example.androidplatform.presentation.history.viewmodel.HistoryViewModel
import com.example.androidplatform.presentation.personal_account.viewmodel.PersonalAccountViewModel
import com.example.androidplatform.presentation.personal_data_by_account_opening.viemodel.PersonalDataByAccountOpeningViewModel
import com.example.androidplatform.presentation.personal_data_by_card_ordering.viewmodel.PersonalDataByCardOrderingViewModel
import com.example.androidplatform.presentation.pin_code.viewmodel.PinCodeViewModel
import com.example.androidplatform.presentation.registration.RegistrationViewModel
import com.example.androidplatform.presentation.restoration_password.RestorePasswordViewModel
import com.example.androidplatform.presentation.stories.viewmodel.SingleStoryViewModel
import com.example.androidplatform.presentation.transaction_info.viewmodel.TransactionInfoViewModel
import com.example.androidplatform.presentation.update_user.UpdateUserViewModel
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
        PersonalDataByCardOrderingViewModel(
            clientInteractor = get(),
            launchOperationInteractor = get(),
            proceedOperationInteractor = get(),
            confirmOperationInteractor = get()
        )
    }

    viewModel {
        PersonalDataByAccountOpeningViewModel(
            clientInteractor = get(),
            launchOperationInteractor = get(),
            proceedOperationInteractor = get(),
            confirmOperationInteractor = get()
        )
    }

    viewModel {
        DashBoardViewModel(
            cardsInteractor = get(),
            accountsInteractor = get(),
            storiesInteractor = get()
        )
    }

    viewModel {
        CardsViewModel()
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

    viewModel {
        UpdateUserViewModel(
            updateUserInteractor = get(),
            clientInteractor = get(),
        )
    }

    viewModel {
        HistoryViewModel(
            historyInteractor = get()
        )
    }

    viewModel {
        TransactionInfoViewModel(
            historyInteractor = get()
        )
    }

    viewModel {
        SingleStoryViewModel(
            storiesInteractor = get()
        )
    }

    viewModel {
        CardInfoViewModel(
            cardsInteractor = get()
        )
    }

    viewModel {
        PinCodeViewModel(
            cardsInteractor = get()
        )
    }
}
