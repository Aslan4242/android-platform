package com.example.androidplatform.di


import com.example.androidplatform.data.network.ITesterApi
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.data.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://itester.online/api/"
private var TOKEN = ""

val dataModule = module {
    single<ITesterApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITesterApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(service = get(), androidContext(), sharedPreferences = get())
    }

//    single<SharedPreferences>  {
//        EncryptedSharedPreferences.create(
//            "encrypted_prefs",
//            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
//            androidContext(),
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//    }
}