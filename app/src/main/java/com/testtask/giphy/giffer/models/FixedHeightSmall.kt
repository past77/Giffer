package com.testtask.giphy.giffer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FixedHeightSmall(
    val height: String,
    val width: String,
    val size: String,
    val url: String,
    @SerializedName("mp4_size")
    val mp4Size: String,
    val mp4: String,
    @SerializedName("webp_size")
    val webpSize: String,
    val webp: String
) : Parcelable
