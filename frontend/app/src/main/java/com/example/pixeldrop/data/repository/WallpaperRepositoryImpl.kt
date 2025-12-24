package com.example.pixeldrop.data.repository

import com.example.pixeldrop.data.remote.PixelDropApi
import com.example.pixeldrop.domain.model.Wallpaper
import com.example.pixeldrop.domain.repository.WallpaperRepository

class WallpaperRepositoryImpl(
    private val api: PixelDropApi
): WallpaperRepository {
    override suspend fun getWallpapers(): List<Wallpaper> {
        val response = api.getWallpapers()

        return response.data.map { dto ->
            Wallpaper(
                id = dto.id,
                title = dto.title,
                category = dto.category ?: "Uncategorized",
                thumbnail_url = dto.thumbnailUrl,
                full_url = dto.fullUrl,
                rawS3Url = dto.imageUrl
            )
        }
    }
    override suspend fun getWallpapersByCategory(category: String): List<Wallpaper> {
        // 1. Fetch FRESH data from Network (No cache)
        val response = api.getWallpapers()

        // 2. Map & Filter immediately
        return response.data
            .map { dto ->
                Wallpaper(
                    id = dto.id,
                    title = dto.title,
                    category = dto.category ?: "Uncategorized",
                    thumbnail_url = dto.thumbnailUrl ?: dto.imageUrl,
                    full_url = dto.fullUrl ?: dto.fullUrl,
                    rawS3Url = dto.imageUrl,
                )
            }
            .filter { it.category.equals(category, ignoreCase = true) }
    }
}