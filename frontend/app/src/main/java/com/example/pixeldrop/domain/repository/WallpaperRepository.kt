package com.example.pixeldrop.domain.repository

import com.example.pixeldrop.domain.model.Wallpaper

interface WallpaperRepository {
    suspend fun getWallpapers(): List<Wallpaper>
    suspend fun getWallpapersByCategory(category: String): List<Wallpaper>
}