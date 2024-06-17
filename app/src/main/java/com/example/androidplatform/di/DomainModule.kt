package com.example.androidplatform.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.androidplatform.data.network.auth.RepositoryAuthenticationImpl
import com.example.androidplatform.data.network.clients.RepositoryClientsImpl
import com.example.androidplatform.domain.RepositoryAuthentication
import com.example.androidplatform.domain.RepositoryClients
import com.example.androidplatform.domain.api.AuthenticationInteractor
import com.example.androidplatform.domain.api.ClientInteractor
import com.example.androidplatform.domain.impl.authentication.AuthenticationInteractorImpl
import com.example.androidplatform.domain.impl.clients.ClientInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {
    single<RepositoryClients> {
        RepositoryClientsImpl(get())
    }

    factory<ClientInteractor> {
        ClientInteractorImpl(get())
    }

    single<AuthenticationInteractor> {
        AuthenticationInteractorImpl(get())
    }

    single<RepositoryAuthentication> {
        RepositoryAuthenticationImpl(get())
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