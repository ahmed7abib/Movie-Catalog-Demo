package com.ahmed.a.habib.moviecatalogapp

import android.app.Application
import com.ahmed.a.habib.moviecatalogapp.di.localModule
import com.ahmed.a.habib.moviecatalogapp.di.networkModule
import com.ahmed.a.habib.moviecatalogapp.di.reposModule
import com.ahmed.a.habib.moviecatalogapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApp)
            modules(
                listOf(
                    networkModule,
                    localModule,
                    reposModule,
                    viewModelModule
                )
            )
        }
    }
}