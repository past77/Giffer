package com.testtask.giphy.giffer.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Images(
    val original: Original,
    @SerializedName("fixed_height_small")
    val fixedHeightSmall: FixedHeightSmall
) : Parcelable