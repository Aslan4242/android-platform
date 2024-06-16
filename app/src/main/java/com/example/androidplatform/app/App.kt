package com.example.androidplatform.app

import android.app.Application
import com.example.androidplatform.di.dataModule
import com.example.androidplatform.di.domainModule
import com.example.androidplatform.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule)
        }
    }
}
