package com.testtask.giphy.giffer.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FixedHeightSmall(
    val height: String,
    val width: String,
    val size: String,
    @SerializedName("url")
    val fixedHeightSmallUrl: String,
) : Parcelable
