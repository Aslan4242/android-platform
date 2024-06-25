package com.example.androidplatform.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.androidplatform.data.network.auth.RepositoryAuthenticationImpl
import com.example.androidplatform.data.network.change_password.RepositoryChangePasswordImpl
import com.example.androidplatform.data.network.clients.RepositoryClientsImpl
import com.example.androidplatform.data.network.logout.RepositoryLogoutImpl
import com.example.androidplatform.data.network.registration.RepositoryRegistrationImpl
import com.example.androidplatform.data.network.restoration_password.RepositoryRestorePasswordImpl
import com.example.androidplatform.domain.RepositoryAuthentication
import com.example.androidplatform.domain.RepositoryChangePassword
import com.example.androidplatform.domain.RepositoryClients
import com.example.androidplatform.domain.RepositoryLogout
import com.example.androidplatform.domain.RepositoryRegistration
import com.example.androidplatform.domain.RepositoryRestorePassword
import com.example.androidplatform.domain.api.AuthenticationInteractor
import com.example.androidplatform.domain.api.ChangePasswordInteractor
import com.example.androidplatform.domain.api.ClientInteractor
import com.example.androidplatform.domain.api.LogoutInteractor
import com.example.androidplatform.domain.api.RegistrationInteractor
import com.example.androidplatform.domain.api.RestorePasswordInteractor
import com.example.androidplatform.domain.impl.authentication.AuthenticationInteractorImpl
import com.example.androidplatform.domain.impl.change_password.ChangePasswordInteractorImpl
import com.example.androidplatform.domain.impl.clients.ClientInteractorImpl
import com.example.androidplatform.domain.impl.logout.LogoutInteractorImpl
import com.example.androidplatform.domain.impl.registration.RegistrationInteractorImpl
import com.example.androidplatform.domain.impl.restore.RestorePasswordInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {
    single<RepositoryClients> {
        RepositoryClientsImpl(get())
    }

    factory<ClientInteractor> {
        ClientInteractorImpl(get())
    }

    single<LogoutInteractor> {
        LogoutInteractorImpl(get())
    }

    single<RepositoryLogout> {
        RepositoryLogoutImpl(get())
    }

    single<RegistrationInteractor> {
        RegistrationInteractorImpl(get())
    }

    single<RepositoryRegistration> {
        RepositoryRegistrationImpl(get())
    }

    single<AuthenticationInteractor> {
        AuthenticationInteractorImpl(get())
    }

    single<RepositoryAuthentication> {
        RepositoryAuthenticationImpl(get())
    }

    single<RestorePasswordInteractor> {
        RestorePasswordInteractorImpl(get())
    }

    single<RepositoryRestorePassword> {
        RepositoryRestorePasswordImpl(get())
    }

    single<ChangePasswordInteractor> {
        ChangePasswordInteractorImpl(get())
    }

    single<RepositoryChangePassword> {
        RepositoryChangePasswordImpl(get())
    }

    single<SharedPreferences> {
        EncryptedSharedPreferences.create(
            "encrypted_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            androidContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}