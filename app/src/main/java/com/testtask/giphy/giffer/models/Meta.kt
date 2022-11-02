package com.testtask.giphy.giffer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meta(
    val status: Int,
    val msg: String,
    @SerializedName("response_id")
    val responseId: String
) : Parcelable