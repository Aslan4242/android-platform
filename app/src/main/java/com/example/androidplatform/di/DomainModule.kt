package com.example.androidplatform.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.androidplatform.data.network.auth.RepositoryAuthenticationImpl
import com.example.androidplatform.data.network.change_password.RepositoryChangePasswordImpl
import com.example.androidplatform.data.network.clients.RepositoryClientsImpl
import com.example.androidplatform.data.network.registration.RepositoryRegistrationImpl
import com.example.androidplatform.data.network.restoration_password.RepositoryRestorePasswordImpl
import com.example.androidplatform.domain.*
import com.example.androidplatform.domain.api.*
import com.example.androidplatform.domain.impl.authentication.AuthenticationInteractorImpl
import com.example.androidplatform.domain.impl.change_password.ChangePasswordInteractorImpl
import com.example.androidplatform.domain.impl.clients.ClientInteractorImpl
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