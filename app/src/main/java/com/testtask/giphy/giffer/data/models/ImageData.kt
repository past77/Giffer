package com.testtask.giphy.giffer.data.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "image_table")
data class ImageData(
    @PrimaryKey
    val id: String,
    val type: String,
    val url: String,
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
    @Embedded
    val images: Images,
) : Parcelable
