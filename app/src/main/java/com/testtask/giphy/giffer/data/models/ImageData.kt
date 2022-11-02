package com.testtask.giphy.giffer.data.models

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val type: String,
    val id: String,
    val uri: String,
    val slug: String,
    @SerializedName("bitly_gif_url")
    val bitlyGifUrl: String,
    @SerializedName("bitly_url")
    val bitlyUrl: String,
    @SerializedName("content_url")
    val contentUrl: String,
    @SerializedName("embed_url")
    val rating: String,
    val title: String,
    val images: Images

) : Parcelable
