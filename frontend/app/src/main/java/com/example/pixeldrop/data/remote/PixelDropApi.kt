package com.example.pixeldrop.data.remote

import retrofit2.http.GET

interface PixelDropApi{
    @GET("/api/wallpapers")
    suspend fun getWallpapers(): ApiResponse<List<WallpaperDto>>
}