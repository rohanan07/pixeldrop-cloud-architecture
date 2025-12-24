package com.example.pixeldrop.domain.usecase

import com.example.pixeldrop.domain.model.Wallpaper
import com.example.pixeldrop.domain.repository.WallpaperRepository

class GetWallpapersUseCase(private val repository: WallpaperRepository) {

    // We update the invoke function to accept an optional category
    suspend operator fun invoke(category: String = "All"): List<Wallpaper> {
        return if (category == "All") {
            repository.getWallpapers()
        } else {
            repository.getWallpapersByCategory(category)
        }
    }
}