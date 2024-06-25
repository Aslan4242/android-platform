package com.example.androidplatform.di


import com.example.androidplatform.BuildConfig
import com.example.androidplatform.data.network.ITesterApi
import com.example.androidplatform.data.network.NetworkClient
import com.example.androidplatform.data.network.RetrofitNetworkClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://itester.online/api/"
private var TOKEN = ""

val dataModule = module {
    single<ITesterApi> {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.HEADERS
                    level = HttpLoggingInterceptor.Level.BODY
                }
            }).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
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