package com.example.pixeldrop.di

import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.pixeldrop.data.remote.PixelDropApi
import com.example.pixeldrop.data.repository.WallpaperRepositoryImpl
import com.example.pixeldrop.domain.repository.WallpaperRepository
import com.example.pixeldrop.domain.usecase.GetWallpapersUseCase
import com.example.pixeldrop.presentation.Home.HomeViewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // 1. Provide Retrofit Instance
    single {
        Retrofit.Builder()
            // CRITICAL: Use 10.0.2.2 for Emulator to talk to Laptop
            .baseUrl("http://pixeldrop-alb-371916997.ap-south-1.elb.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 2. Provide the API Service
    single {
        get<Retrofit>().create(PixelDropApi::class.java)
    }

    single<WallpaperRepository> {
        WallpaperRepositoryImpl(get()) // get() injects the API
    }

    factory { GetWallpapersUseCase(get()) }

    viewModel { HomeViewModel(get()) }
}