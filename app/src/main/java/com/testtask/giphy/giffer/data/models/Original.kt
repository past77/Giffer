package com.testtask.giphy.giffer.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Original(
    @SerializedName("height")
    val originalHeight: String,
    @SerializedName("width")
    val originalWidth: String,
    @SerializedName("size")
    val originalSize: String,
    @SerializedName("url")
    val originalUrl: String,
    @SerializedName("mp4_size")
    val mp4Size: String,
    val mp4: String,
    @SerializedName("webp_size")
    val webpSize: String,
    val webp: String,
    val frames: String,
    val hash: String
) : Parcelable