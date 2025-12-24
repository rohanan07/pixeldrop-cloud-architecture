package com.example.pixeldrop.data.remote

import com.google.gson.annotations.SerializedName

data class WallpaperDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String?,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String,
    @SerializedName("fullUrl") val fullUrl: String,
    @SerializedName("created_at") val createdAt: String
)