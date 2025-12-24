package com.example.pixeldrop

import android.app.Application
import com.example.pixeldrop.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PixelDropApplication(): Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PixelDropApplication)
            modules(appModule)
        }
    }
}