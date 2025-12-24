package com.example.pixeldrop.domain.model

import kotlinx.serialization.SerialName

data class Wallpaper(
    val id: Int,
    val title: String,
    val category: String,
    val thumbnail_url: String,
    val full_url: String,
    @SerialName("image_url")
    val rawS3Url: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)