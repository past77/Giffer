package com.testtask.giphy.giffer.data.models

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Images(
    @Embedded
    val original: Original,

    @Embedded
    @SerializedName("fixed_height_small")
    val fixedHeightSmall: FixedHeightSmall
) : Parcelable